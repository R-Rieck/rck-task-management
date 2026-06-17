import { ApolloClient, InMemoryCache } from '@apollo/client/core'
import { HttpLink } from '@apollo/client/link/http'

const httpLink = new HttpLink({ uri: '/graphql' })

export const client = new ApolloClient({
  link: httpLink,
  cache: new InMemoryCache(),
})
