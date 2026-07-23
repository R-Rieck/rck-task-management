import { useState } from 'react'
import { Typography, Spin, Alert, Table, Button, Modal, Form, Input, Select, message } from 'antd'
import { DeleteOutlined, PlusOutlined, EditOutlined } from '@ant-design/icons'
import { useParams } from 'react-router-dom'
import { useQuery, useMutation } from '@apollo/client/react'
import { AppLayout } from '../components/AppLayout'
import { GET_BOARD, CREATE_BOARD_SECTION, DELETE_BOARD_SECTION, RENAME_BOARD_SECTION } from '../graphql/board'

const { Title, Text } = Typography

export function BoardView() {
  const { boardId } = useParams()
  const [selectedSectionId, setSelectedSectionId] = useState<string | null>(null)
  const [addModalOpen, setAddModalOpen] = useState(false)
  const [renameModalOpen, setRenameModalOpen] = useState(false)
  const [addForm] = Form.useForm()
  const [renameForm] = Form.useForm()

  const { data, loading, error, refetch } = useQuery(GET_BOARD, {
    variables: { id: boardId },
  })

  const [createSection] = useMutation(CREATE_BOARD_SECTION)
  const [deleteSection] = useMutation(DELETE_BOARD_SECTION)
  const [renameSection] = useMutation(RENAME_BOARD_SECTION)

  if (loading) return <AppLayout><Spin size="large" style={{ display: 'block', margin: '40px auto' }} /></AppLayout>
  if (error) return <AppLayout><Alert type="error" message="Failed to load board" /></AppLayout>

  const board = (data as any)?.board
  const sections = (board.sections ?? []) as Array<{ id: string; name: string; position: number }>
  const selectedSection = sections.find((s) => s.id === selectedSectionId) ?? null
  const sorted = [...sections].sort((a, b) => a.position - b.position)

  const handleSelectSection = (e: React.MouseEvent, id: string) => {
    e.stopPropagation()
    setSelectedSectionId((prev) => (prev === id ? null : id))
  }

  const handleAddSection = async (values: { name: string; position: number | undefined }) => {
    try {
      await createSection({
        variables: {
          input: {
            boardId,
            name: values.name,
            position: values.position !== undefined ? values.position : null,
          },
        },
      })
      setAddModalOpen(false)
      addForm.resetFields()
      refetch()
      message.success('Section created')
    } catch (err: unknown) {
      message.error(err instanceof Error ? err.message : 'Failed to create section')
    }
  }

  const handleDeleteSection = () => {
    if (!selectedSection) return
    Modal.confirm({
      title: 'Delete this section?',
      content: `Are you sure you want to delete "${selectedSection.name}"?`,
      okText: 'Delete',
      okButtonProps: { danger: true },
      onOk: async () => {
        try {
          await deleteSection({ variables: { sectionId: selectedSection.id } })
          setSelectedSectionId(null)
          refetch()
          message.success('Section deleted')
        } catch (err: unknown) {
          message.error(err instanceof Error ? err.message : 'Failed to delete section')
        }
      },
    })
  }

  const handleRenameSection = async (values: { name: string }) => {
    if (!selectedSection) return
    try {
      await renameSection({ variables: { sectionId: selectedSection.id, name: values.name } })
      setRenameModalOpen(false)
      renameForm.resetFields()
      refetch()
      message.success('Section renamed')
    } catch (err: unknown) {
      message.error(err instanceof Error ? err.message : 'Failed to rename section')
    }
  }

  const openRenameModal = () => {
    if (selectedSection) {
      renameForm.setFieldsValue({ name: selectedSection.name })
      setRenameModalOpen(true)
    }
  }

  const positionOptions = [
    ...[...sections]
      .sort((a, b) => a.position - b.position)
      .map((s, i) => ({ value: i, label: `${s.position} — After "${s.name}"` })),
    { value: sections.length, label: `At the end` },
  ]

  return (
    <AppLayout>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 24 }}>
        <Title level={2} style={{ margin: 0 }}>{board.name}</Title>
      </div>

      {sections.length === 0 ? (
        <div style={{ textAlign: 'center', marginTop: 40 }}>
          <Text type="secondary" style={{ display: 'block', marginBottom: 16 }}>No sections yet.</Text>
          <Button type="primary" icon={<PlusOutlined />} onClick={() => setAddModalOpen(true)}>
            Create First Section
          </Button>
        </div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }} onClick={() => setSelectedSectionId(null)}>
          {sorted.map((section) => {
            const isSelected = section.id === selectedSectionId
            return (
              <div
                key={section.id}
                style={{
                  padding: 12, borderRadius: 8,
                  border: isSelected ? '2px solid #1677ff' : '2px solid transparent',
                  background: isSelected ? '#e6f4ff' : 'transparent',
                  transition: 'border-color 0.2s, background 0.2s',
                }}
              >
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 8 }}>
                  <Text
                    strong
                    style={{ fontSize: 16, cursor: 'pointer', userSelect: 'none' }}
                    onClick={(e) => handleSelectSection(e, section.id)}
                  >
                    {section.name}
                  </Text>
                </div>
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
                  locale={{ emptyText: (
                    <Text type="secondary" style={{ cursor: 'pointer' }}>
                      <PlusOutlined style={{ marginRight: 4 }} />Add ticket
                    </Text>
                  ) }}
                  size="small"
                />
              </div>
            )
          })}
          <div style={{ textAlign: 'center' }}>
            <Button type="dashed" icon={<PlusOutlined />} onClick={() => setAddModalOpen(true)}>
              Add Section
            </Button>
          </div>
        </div>
      )}

      {selectedSection && (
        <div
          style={{
            position: 'fixed', bottom: 24, left: '50%', transform: 'translateX(-50%)',
            background: '#fff', borderRadius: 8, boxShadow: '0 4px 16px rgba(0,0,0,0.12)',
            padding: '8px 16px', display: 'flex', alignItems: 'center', gap: 12,
            zIndex: 1000, border: '1px solid #e8e8e8',
          }}
        >
          <Text style={{ marginRight: 8 }}>
            <strong>{selectedSection.name}</strong>
          </Text>
          <Button icon={<EditOutlined />} onClick={openRenameModal}>
            Rename
          </Button>
          <Button danger icon={<DeleteOutlined />} onClick={handleDeleteSection}>
            Delete
          </Button>
        </div>
      )}

      <Modal
        title="Add Section"
        open={addModalOpen}
        onCancel={() => { setAddModalOpen(false); addForm.resetFields() }}
        onOk={() => addForm.submit()}
        okText="Create"
      >
        <Form form={addForm} layout="vertical" onFinish={handleAddSection}>
          <Form.Item name="name" label="Section name" rules={[{ required: true, message: 'Enter a section name' }]}>
            <Input placeholder="Section name" />
          </Form.Item>
          <Form.Item name="position" label="Position">
            <Select placeholder="Choose position" options={positionOptions} allowClear />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="Rename Section"
        open={renameModalOpen}
        onCancel={() => { setRenameModalOpen(false); renameForm.resetFields() }}
        onOk={() => renameForm.submit()}
        okText="Save"
      >
        <Form form={renameForm} layout="vertical" onFinish={handleRenameSection}>
          <Form.Item name="name" label="Name" rules={[{ required: true, message: 'Enter a name' }]}>
            <Input placeholder="Section name" />
          </Form.Item>
        </Form>
      </Modal>
    </AppLayout>
  )
}
