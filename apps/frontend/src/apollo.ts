import { ApolloClient, InMemoryCache, ApolloLink, Observable } from '@apollo/client'
import { HttpLink } from '@apollo/client/link/http'
import { setContext } from '@apollo/client/link/context'
import { onError } from '@apollo/client/link/error'
import { CombinedGraphQLErrors } from '@apollo/client/errors'

const httpLink = new HttpLink({ uri: '/graphql' })

const authLink = setContext((_, { headers }) => {
  const accessToken = localStorage.getItem('accessToken')
  return {
    headers: {
      ...headers,
      ...(accessToken ? { Authorization: `Bearer ${accessToken}` } : {}),
    },
  }
})

let refreshPromise: Promise<void> | null = null

async function performRefresh(): Promise<void> {
  const refreshToken = localStorage.getItem('refreshToken')
  if (!refreshToken) throw new Error('No refresh token')

  const response = await fetch('/graphql', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      query: `mutation Refresh($input: RefreshAuthenticationInput!) {
        refresh(input: $input) { accessToken refreshToken }
      }`,
      variables: { input: { refreshToken } },
    }),
  })

  if (!response.ok) {
    throw new Error(`Refresh failed with status ${response.status}`)
  }

  const json = await response.json()
  if (json.errors) throw new Error(json.errors[0].message)

  const { accessToken, refreshToken: newRefreshToken } = json.data.refresh
  localStorage.setItem('accessToken', accessToken)
  localStorage.setItem('refreshToken', newRefreshToken)
}

const errorLink = onError(({ error, operation, forward }) => {
  const isAuthError = CombinedGraphQLErrors.is(error) &&
    error.errors.some((err) => err.extensions?.['httpStatus'] === 401)

  if (!isAuthError) return
  if (!localStorage.getItem('refreshToken')) return

  if (!refreshPromise) {
    refreshPromise = performRefresh().catch((err: unknown) => {
      localStorage.removeItem('accessToken')
      localStorage.removeItem('refreshToken')
      localStorage.removeItem('currentAccountId')
      window.location.href = '/login'
      throw err
    }).finally(() => {
      refreshPromise = null
    })
  }

  return new Observable<ApolloLink.Result>((observer) => {
    refreshPromise!
      .then(() => {
        const newToken = localStorage.getItem('accessToken')
        if (!newToken) throw new Error('No token after refresh')
        operation.setContext(({ headers = {} }) => ({
          headers: {
            ...headers,
            Authorization: `Bearer ${newToken}`,
          },
        }))
      })
      .then(() => forward(operation).subscribe(observer))
      .catch((err: unknown) => observer.error(err))
  })
})

export const client = new ApolloClient({
  link: ApolloLink.from([errorLink, authLink, httpLink]),
  cache: new InMemoryCache(),
})

export function clearAuthCache() {
  client.resetStore()
}
