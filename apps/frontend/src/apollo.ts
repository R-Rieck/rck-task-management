import { ApolloClient, InMemoryCache } from '@apollo/client/core'
import { HttpLink } from '@apollo/client/link/http'
import { setContext } from '@apollo/client/link/context'

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

export const client = new ApolloClient({
  link: authLink.concat(httpLink),
  cache: new InMemoryCache(),
})

export function clearAuthCache() {
  client.resetStore()
}
