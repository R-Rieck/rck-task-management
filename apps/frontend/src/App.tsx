import { gql } from '@apollo/client'
import { useQuery } from '@apollo/client/react'

const PING = gql`
  query Ping {
    __typename
  }
`

function App() {
  const { loading, error } = useQuery(PING)

  return (
    <div style={{ padding: '2rem', fontFamily: 'system-ui, sans-serif' }}>
      <h1>Task Management</h1>
      {loading && <p>Connecting to backend…</p>}
      {error && <p style={{ color: 'red' }}>Backend unreachable: {error.message}</p>}
      {!loading && !error && <p style={{ color: 'green' }}>Backend connected ✓</p>}
    </div>
  )
}

export default App
