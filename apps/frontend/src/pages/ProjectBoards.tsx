import { Typography, Card, Spin, Alert } from 'antd'
import { useParams, useNavigate } from 'react-router-dom'
import { useQuery } from '@apollo/client/react'
import { AppLayout } from '../components/AppLayout'
import { GET_PROJECTS } from '../graphql/project'

const { Title, Text } = Typography

export function ProjectBoards() {
  const { projectId } = useParams()
  const navigate = useNavigate()
  const { data, loading, error } = useQuery(GET_PROJECTS)

  if (loading) return <AppLayout><Spin size="large" style={{ display: 'block', margin: '40px auto' }} /></AppLayout>
  if (error) return <AppLayout><Alert type="error" message="Failed to load" /></AppLayout>

  const project = (data as any)?.projects?.find((p: any) => p.id === projectId)
  if (!project) return <AppLayout><Alert type="warning" message="Project not found" /></AppLayout>

  return (
    <AppLayout>
      <Title level={2}>{project.name}</Title>
      <Text type="secondary" style={{ display: 'block', marginBottom: 24 }}>
        {project.description || 'No description'}
      </Text>
      <div style={{ display: 'flex', gap: 16, flexWrap: 'wrap' }}>
        {project.boards?.map((board: any) => (
          <Card
            key={board.id}
            hoverable
            style={{ width: 240 }}
            onClick={() => navigate(`/boards/${board.id}`)}
          >
            <Card.Meta title={board.name} />
            <div style={{ marginTop: 8 }}>
              <Text type="secondary">{board.sections?.length ?? 0} sections</Text>
            </div>
          </Card>
        ))}
        {(!project.boards || project.boards.length === 0) && (
          <Text type="secondary">No boards yet. Create one from the sidebar.</Text>
        )}
      </div>
    </AppLayout>
  )
}
