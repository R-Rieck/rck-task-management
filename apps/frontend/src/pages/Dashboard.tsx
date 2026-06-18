import { Typography } from 'antd'
import { AppLayout } from '../components/AppLayout'
import { useAuth } from '../auth/AuthContext'

const { Title, Text } = Typography

export function Dashboard() {
  const { user } = useAuth()

  return (
    <AppLayout>
      <Title level={2}>Welcome, {user?.name}</Title>
      <Text type="secondary">Select a project or create a new one to get started.</Text>
    </AppLayout>
  )
}
