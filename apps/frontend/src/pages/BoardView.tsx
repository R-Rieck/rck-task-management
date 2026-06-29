import { useState } from 'react'
import { Typography, Spin, Alert, Segmented, Table } from 'antd'
import { AppstoreOutlined, UnorderedListOutlined } from '@ant-design/icons'
import { useParams } from 'react-router-dom'
import { useQuery } from '@apollo/client/react'
import { AppLayout } from '../components/AppLayout'
import { GET_BOARD } from '../graphql/board'

const { Title, Text } = Typography

function KanbanView({ sections }: { sections: any[] }) {
  return (
    <div style={{ display: 'flex', gap: 16, overflow: 'auto', paddingBottom: 16, flex: 1 }}>
      {sections.map((section) => (
        <div
          key={section.id}
          style={{
            minWidth: 260, maxWidth: 320, flex: 1,
            background: '#f0f0f0', borderRadius: 8,
            padding: 12,
          }}
        >
          <Text strong style={{ display: 'block', marginBottom: 8, padding: '0 4px' }}>
            {section.name}
          </Text>
          <div style={{ minHeight: 120 }}>
            {/* Cards will go here later */}
          </div>
        </div>
      ))}
    </div>
  )
}

function ListView({ sections }: { sections: any[] }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
      {sections.map((section) => (
        <div key={section.id}>
          <Text strong style={{ display: 'block', fontSize: 16, marginBottom: 8, padding: '4px 0' }}>
            {section.name}
          </Text>
          <Table
            columns={[
              { title: 'Title', dataIndex: 'title', key: 'title' },
              { title: 'Assignee', dataIndex: 'assignee', key: 'assignee' },
              { title: 'Status', dataIndex: 'status', key: 'status' },
            ]}
            dataSource={[]}
            pagination={false}
            bordered={false}
            showHeader={false}
            locale={{ emptyText: 'No tickets yet' }}
            size="small"
          />
        </div>
      ))}
    </div>
  )
}

export function BoardView() {
  const { boardId } = useParams()
  const [view, setView] = useState<string | number>('kanban')
  const { data, loading, error } = useQuery(GET_BOARD, {
    variables: { id: boardId },
  })

  if (loading) return <AppLayout><Spin size="large" style={{ display: 'block', margin: '40px auto' }} /></AppLayout>
  if (error) return <AppLayout><Alert type="error" message="Failed to load board" /></AppLayout>

  const board = (data as any)?.board

  return (
    <AppLayout>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 24 }}>
        <Title level={2} style={{ margin: 0 }}>{board.name}</Title>
        <Segmented
          value={view}
          onChange={setView}
          options={[
            { value: 'kanban', icon: <AppstoreOutlined />, label: 'Kanban' },
            { value: 'list', icon: <UnorderedListOutlined />, label: 'List' },
          ]}
        />
      </div>

      {board.sections?.length === 0 ? (
        <Text type="secondary">No sections yet.</Text>
      ) : view === 'kanban' ? (
        <KanbanView sections={board.sections ?? []} />
      ) : (
        <ListView sections={board.sections ?? []} />
      )}
    </AppLayout>
  )
}
