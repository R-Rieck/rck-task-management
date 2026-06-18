import type { ReactNode } from 'react'
import { Layout, Typography } from 'antd'
import { Link } from 'react-router-dom'
import { UserMenu } from './UserMenu'

const { Header, Content } = Layout
const { Title } = Typography

export function AppLayout({ children }: { children: ReactNode }) {
  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Header
        style={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          background: '#001529',
          padding: '0 24px',
        }}
      >
        <Link to="/" style={{ color: '#fff', textDecoration: 'none' }}>
          <Title level={4} style={{ color: '#fff', margin: 0 }}>
            Task Management
          </Title>
        </Link>
        <UserMenu />
      </Header>
      <Content style={{ padding: 24 }}>{children}</Content>
    </Layout>
  )
}
