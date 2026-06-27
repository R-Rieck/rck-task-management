import { useState, type ReactNode } from 'react'
import { Layout, Menu, Typography, Tree, Button, Modal, Form, Input, Switch, Spin, message, Dropdown, ConfigProvider } from 'antd'
import {
  DashboardOutlined,
  FolderOutlined,
  StarOutlined,
  HeartOutlined,
  BookOutlined,
  SettingOutlined,
  FlagOutlined,
  HomeOutlined,
  CodeOutlined,
  MailOutlined,
  FileOutlined,
  RocketOutlined,
  SmileOutlined,
  PlusOutlined,
  EditOutlined,
  DeleteOutlined,
  EllipsisOutlined,
  TeamOutlined,
  CheckOutlined,
  DownOutlined,
} from '@ant-design/icons'
import { useQuery, useMutation } from '@apollo/client/react'
import { useNavigate, useLocation, Link } from 'react-router-dom'
import { UserMenu } from './UserMenu'
import { useAuth } from '../auth/AuthContext'
import { GET_PROJECTS, CREATE_PROJECT, EDIT_PROJECT, DELETE_PROJECT } from '../graphql/project'
import { GET_USER_ACCOUNTS } from '../graphql/account'

const { Sider, Content } = Layout

const PROJECT_ICONS: Record<string, React.ReactNode> = {
  FolderOutlined: <FolderOutlined />,
  StarOutlined: <StarOutlined />,
  HeartOutlined: <HeartOutlined />,
  BookOutlined: <BookOutlined />,
  SettingOutlined: <SettingOutlined />,
  FlagOutlined: <FlagOutlined />,
  HomeOutlined: <HomeOutlined />,
  CodeOutlined: <CodeOutlined />,
  MailOutlined: <MailOutlined />,
  FileOutlined: <FileOutlined />,
  RocketOutlined: <RocketOutlined />,
  SmileOutlined: <SmileOutlined />,
}

function renderIcon(iconName: string | null | undefined) {
  return iconName && PROJECT_ICONS[iconName] ? PROJECT_ICONS[iconName] : <FolderOutlined />
}

function IconPicker({ value, onChange }: { value?: string | null; onChange?: (val: string) => void }) {
  const iconNames = Object.keys(PROJECT_ICONS)
  return (
    <div style={{ display: 'flex', flexWrap: 'wrap', gap: 6 }}>
      {iconNames.map((name) => (
        <div
          key={name}
          onClick={() => onChange?.(name)}
          style={{
            width: 32, height: 32, display: 'flex', alignItems: 'center', justifyContent: 'center',
            borderRadius: 4, cursor: 'pointer', fontSize: 18,
            border: value === name ? '2px solid #1677ff' : '2px solid transparent',
            background: value === name ? '#e6f4ff' : '#f5f5f5',
          }}
        >
          {PROJECT_ICONS[name]}
        </div>
      ))}
    </div>
  )
}

function ProjectNodeTitle({
  project,
  onEdit,
  onDelete,
}: {
  project: { id: string; name: string; icon: string | null }
  onEdit: () => void
  onDelete: () => void
}) {
  const [hovered, setHovered] = useState(false)

  const dropdownItems = [
    { key: 'edit', icon: <EditOutlined />, label: 'Edit' },
    { key: 'delete', icon: <DeleteOutlined />, label: 'Delete', danger: true },
  ]

  return (
    <div
      onMouseEnter={() => setHovered(true)}
      onMouseLeave={() => setHovered(false)}
      style={{ display: 'flex', alignItems: 'center', gap: 6, paddingRight: 4 }}
    >
      <span style={{ color: 'rgba(255,255,255,0.45)', fontSize: 14, flexShrink: 0, lineHeight: 1 }}>
        {renderIcon(project.icon)}
      </span>
      <span style={{ color: 'rgba(255,255,255,0.85)', fontSize: 14, flex: 1, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
        {project.name}
      </span>
      <span
        style={{ opacity: hovered ? 1 : 0, transition: 'opacity 0.15s', flexShrink: 0 }}
        onClick={(e) => e.stopPropagation()}
      >
        <Dropdown
          menu={{
            items: dropdownItems,
            onClick: ({ key }) => {
              if (key === 'edit') onEdit()
              else if (key === 'delete') {
                Modal.confirm({
                  title: 'Delete this project?',
                  content: `Are you sure you want to delete "${project.name}"?`,
                  okText: 'Delete',
                  okButtonProps: { danger: true },
                  onOk: onDelete,
                })
              }
            },
          }}
          trigger={['click']}
        >
          <Button type="text" size="small" icon={<EllipsisOutlined />} style={{ color: 'rgba(255,255,255,0.45)' }} />
        </Dropdown>
      </span>
    </div>
  )
}

export function AppLayout({ children }: { children: ReactNode }) {
  const navigate = useNavigate()
  const location = useLocation()
  const { currentAccountId, switchAccount } = useAuth()

  const { data: projectsData, loading: projectsLoading } = useQuery(GET_PROJECTS)
  const { data: accountsData } = useQuery(GET_USER_ACCOUNTS)
  const [createProject] = useMutation(CREATE_PROJECT, { refetchQueries: ['Projects'] })
  const [editProject] = useMutation(EDIT_PROJECT, { refetchQueries: ['Projects'] })
  const [deleteProject] = useMutation(DELETE_PROJECT, { refetchQueries: ['Projects'] })

  const projects = (projectsData as { projects: any[] } | undefined)?.projects ?? []
  const accounts = (accountsData as { getUserAccounts: Array<{ id: string; name: string }> } | undefined)?.getUserAccounts ?? []
  const currentAccount = accounts.find((a) => a.id === currentAccountId)

  const [createOpen, setCreateOpen] = useState(false)
  const [createForm] = Form.useForm()
  const [editingProject, setEditingProject] = useState<{ id: string; name: string; description: string | null; isPrivate: boolean; icon: string | null } | null>(null)
  const [editForm] = Form.useForm()

  const handleCreate = async (values: { name: string; description?: string; isPrivate?: boolean; icon?: string }) => {
    try {
      await createProject({
        variables: {
          input: {
            name: values.name,
            description: values.description || null,
            isPrivate: values.isPrivate ?? false,
            icon: values.icon || null,
          },
        },
      })
      setCreateOpen(false)
      createForm.resetFields()
      message.success('Project created')
    } catch (err: unknown) {
      message.error(err instanceof Error ? err.message : 'Failed to create project')
    }
  }

  const handleEdit = async (values: { name: string; description?: string; isPrivate: boolean; icon?: string }) => {
    if (!editingProject) return
    try {
      await editProject({
        variables: {
          input: {
            projectId: editingProject.id,
            name: values.name,
            description: values.description || null,
            isPrivate: values.isPrivate,
            icon: values.icon || null,
          },
        },
      })
      setEditingProject(null)
      editForm.resetFields()
      message.success('Project updated')
    } catch (err: unknown) {
      message.error(err instanceof Error ? err.message : 'Failed to update project')
    }
  }

  const handleDelete = async (projectId: string) => {
    try {
      await deleteProject({ variables: { projectId } })
      message.success('Project deleted')
    } catch (err: unknown) {
      message.error(err instanceof Error ? err.message : 'Failed to delete project')
    }
  }

  const treeData = projects.map((p: { id: string; name: string; description: string | null; isPrivate: boolean; icon: string | null }) => ({
    key: p.id,
    title: (
      <ProjectNodeTitle
        project={p}
        onEdit={() => {
          setEditingProject(p)
          editForm.setFieldsValue({ name: p.name, description: p.description, isPrivate: p.isPrivate, icon: p.icon })
        }}
        onDelete={() => handleDelete(p.id)}
      />
    ),
  }))

  const accountDropdownItems = accounts.map((acct) => ({
    key: acct.id,
    icon: acct.id === currentAccountId ? <CheckOutlined /> : undefined,
    label: acct.name,
    disabled: acct.id === currentAccountId,
  }))

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider width={280} style={{ background: '#001529' }}>
        <div style={{ height: '100vh', display: 'flex', flexDirection: 'column', overflow: 'hidden' }}>
          <div style={{ padding: '16px 24px' }}>
            <Link to="/" style={{ color: '#fff', fontSize: 18, fontWeight: 600, textDecoration: 'none' }}>
              Task Management
            </Link>
          </div>

          <Menu
            mode="inline"
            theme="dark"
            selectedKeys={[location.pathname]}
            onClick={({ key }) => navigate(key)}
            items={[
              { key: '/', icon: <DashboardOutlined />, label: 'Dashboard' },
            ]}
            style={{ background: 'transparent', borderRight: 0 }}
          />

          <div style={{ height: 1, background: 'rgba(255,255,255,0.1)', margin: '8px 16px' }} />

          <div style={{ padding: '8px 16px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <Typography.Text style={{ color: 'rgba(255,255,255,0.65)', fontSize: 12, textTransform: 'uppercase', letterSpacing: 1 }}>
              Projects
            </Typography.Text>
            <Button type="text" size="small" icon={<PlusOutlined />} onClick={() => setCreateOpen(true)} style={{ color: 'rgba(255,255,255,0.65)' }} />
          </div>

          <div style={{ flex: 1, minHeight: 0, overflow: 'auto', padding: '0 8px' }}>
            {projectsLoading ? (
              <Spin size="small" style={{ display: 'block', margin: '16px auto' }} />
            ) : (
              <ConfigProvider
                theme={{
                  components: {
                    Tree: {
                      nodeSelectedBg: 'rgba(255,255,255,0.1)',
                      nodeSelectedColor: '#fff',
                      nodeHoverBg: 'rgba(255,255,255,0.06)',
                      nodeHoverColor: 'rgba(255,255,255,0.85)',
                    },
                  },
                }}
              >
                <Tree
                  treeData={treeData}
                  blockNode
                  style={{ color: '#fff', background: 'transparent' }}
                  styles={{
                    root: { background: 'transparent' },
                    itemTitle: { background: 'transparent', width: '100%' },
                    item: { background: 'transparent', width: '100%' },
                  }}
                />
              </ConfigProvider>
            )}
          </div>

          <div style={{ padding: '14px 16px 16px', borderTop: '1px solid rgba(255,255,255,0.1)' }}>
            <Dropdown
              menu={{ items: accountDropdownItems, onClick: ({ key }) => switchAccount(key) }}
              trigger={['click']}
            >
              <div style={{
                cursor: 'pointer', display: 'flex', alignItems: 'center', gap: 10,
                border: '1px solid rgba(255,255,255,0.15)', borderRadius: 6,
                padding: '8px 12px', marginBottom: 12,
                background: 'rgba(255,255,255,0.04)',
                color: 'rgba(255,255,255,0.85)', fontSize: 14,
              }}>
                <TeamOutlined style={{ fontSize: 14, color: 'rgba(255,255,255,0.45)', flexShrink: 0 }} />
                <span style={{ flex: 1, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                  {currentAccount?.name ?? 'Switch Account'}
                </span>
                <DownOutlined style={{ fontSize: 10, color: 'rgba(255,255,255,0.45)', flexShrink: 0 }} />
              </div>
            </Dropdown>
            <UserMenu />
          </div>
        </div>
      </Sider>

      <Content style={{ padding: 24, background: '#f5f5f5', minHeight: '100vh' }}>
        {children}
      </Content>

      <Modal
        title="Create Project"
        open={createOpen}
        onCancel={() => { setCreateOpen(false); createForm.resetFields() }}
        onOk={() => createForm.submit()}
        okText="Create"
      >
        <Form form={createForm} layout="vertical" onFinish={handleCreate} initialValues={{ isPrivate: false }}>
          <Form.Item name="name" label="Name" rules={[{ required: true, message: 'Enter a project name' }]}>
            <Input placeholder="Project name" />
          </Form.Item>
          <Form.Item name="description" label="Description">
            <Input.TextArea rows={3} placeholder="Optional description" />
          </Form.Item>
          <Form.Item name="icon" label="Icon">
            <IconPicker />
          </Form.Item>
          <Form.Item name="isPrivate" label="Private project" valuePropName="checked">
            <Switch />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="Edit Project"
        open={!!editingProject}
        onCancel={() => { setEditingProject(null); editForm.resetFields() }}
        onOk={() => editForm.submit()}
        okText="Save"
      >
        <Form form={editForm} layout="vertical" onFinish={handleEdit}>
          <Form.Item name="name" label="Name" rules={[{ required: true, message: 'Enter a project name' }]}>
            <Input placeholder="Project name" />
          </Form.Item>
          <Form.Item name="description" label="Description">
            <Input.TextArea rows={3} placeholder="Optional description" />
          </Form.Item>
          <Form.Item name="icon" label="Icon">
            <IconPicker />
          </Form.Item>
          <Form.Item name="isPrivate" label="Private project" valuePropName="checked">
            <Switch />
          </Form.Item>
        </Form>
      </Modal>
    </Layout>
  )
}

