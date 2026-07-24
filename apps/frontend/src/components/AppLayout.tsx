import { useState, type ReactNode } from 'react'
import { Layout, Menu, Typography, Tree, Button, Modal, Form, Input, Select, Spin, message, Dropdown, ConfigProvider } from 'antd'
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
  ProjectOutlined,
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
import { CREATE_BOARD, EDIT_BOARD, DELETE_BOARD } from '../graphql/board'
import { GET_USER_ACCOUNTS, GET_MEMBERS } from '../graphql/account'

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
  onCreateBoard,
}: {
  project: { id: string; name: string; icon: string | null }
  onEdit: () => void
  onDelete: () => void
  onCreateBoard: () => void
}) {
  const [hovered, setHovered] = useState(false)

  const dropdownItems = [
    { key: 'createBoard', icon: <PlusOutlined />, label: 'Create Board' },
    { type: 'divider' as const },
    { key: 'edit', icon: <EditOutlined />, label: 'Edit' },
    { key: 'delete', icon: <DeleteOutlined />, label: 'Delete', danger: true },
  ]

  const handleMenuClick = ({ key }: { key: string }) => {
    if (key === 'createBoard') onCreateBoard()
    else if (key === 'edit') onEdit()
    else if (key === 'delete') {
      Modal.confirm({
        title: 'Delete this project?',
        content: `Are you sure you want to delete "${project.name}"?`,
        okText: 'Delete',
        okButtonProps: { danger: true },
        onOk: onDelete,
      })
    }
  }

  const rowContent = (
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
        <Dropdown menu={{ items: dropdownItems, onClick: handleMenuClick }} trigger={['click']}>
          <Button type="text" size="small" icon={<EllipsisOutlined />} style={{ color: 'rgba(255,255,255,0.45)' }} />
        </Dropdown>
      </span>
    </div>
  )

  return (
    <Dropdown menu={{ items: dropdownItems, onClick: handleMenuClick }} trigger={['contextMenu']}>
      {rowContent}
    </Dropdown>
  )
}

function BoardNodeTitle({
  name,
  canDelete,
  onEdit,
  onDelete,
}: {
  name: string
  canDelete: boolean
  onEdit: () => void
  onDelete: () => void
}) {
  const [hovered, setHovered] = useState(false)

  const items = [
    { key: 'edit', icon: <EditOutlined />, label: 'Edit' },
    ...(canDelete ? [{ key: 'delete', icon: <DeleteOutlined />, label: 'Delete', danger: true } as const] : []),
  ]

  const handleMenuClick = ({ key }: { key: string }) => {
    if (key === 'edit') onEdit()
    else if (key === 'delete') {
      Modal.confirm({
        title: 'Delete this board?',
        content: `Are you sure you want to delete "${name}"?`,
        okText: 'Delete',
        okButtonProps: { danger: true },
        onOk: onDelete,
      })
    }
  }

  const actionButton = (
    <span
      style={{ opacity: hovered ? 1 : 0, transition: 'opacity 0.15s', flexShrink: 0 }}
      onClick={(e) => e.stopPropagation()}
    >
      <Dropdown menu={{ items: items.length > 0 ? items : [{ key: 'edit', icon: <EditOutlined />, label: 'Edit' }], onClick: handleMenuClick }} trigger={['click']}>
        <Button type="text" size="small" icon={<EllipsisOutlined />} style={{ color: 'rgba(255,255,255,0.45)' }} />
      </Dropdown>
    </span>
  )

  const rowContent = (
    <div
      onMouseEnter={() => setHovered(true)}
      onMouseLeave={() => setHovered(false)}
      style={{ display: 'flex', alignItems: 'center', gap: 6, paddingRight: 4 }}
    >
      <span style={{ color: 'rgba(255,255,255,0.85)', fontSize: 14, flex: 1, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
        {name}
      </span>
      {actionButton}
    </div>
  )

  if (!canDelete) return rowContent

  return (
    <Dropdown menu={{ items, onClick: handleMenuClick }} trigger={['contextMenu']}>
      {rowContent}
    </Dropdown>
  )
}

export function AppLayout({ children }: { children: ReactNode }) {
  const navigate = useNavigate()
  const location = useLocation()
  const { user, currentAccountId, switchAccount } = useAuth()

  const { data: projectsData, loading: projectsLoading } = useQuery(GET_PROJECTS)
  const { data: accountsData } = useQuery(GET_USER_ACCOUNTS)
  const { data: membersData } = useQuery(GET_MEMBERS)
  const [createProject] = useMutation(CREATE_PROJECT, { refetchQueries: ['Projects'] })
  const [editProject] = useMutation(EDIT_PROJECT, { refetchQueries: ['Projects'] })
  const [deleteProject] = useMutation(DELETE_PROJECT, { refetchQueries: ['Projects'] })
  const [createBoard] = useMutation(CREATE_BOARD, { refetchQueries: ['Projects'] })
  const [editBoard] = useMutation(EDIT_BOARD, { refetchQueries: ['Projects'] })
  const [deleteBoard] = useMutation(DELETE_BOARD, { refetchQueries: ['Projects'] })

  const projects = (projectsData as any)?.projects ?? []
  const accounts = (accountsData as { getUserAccounts: Array<{ id: string; name: string }> } | undefined)?.getUserAccounts ?? []
  const accountMembers = ((membersData as any)?.getMembers?.members ?? []) as Array<{ id: string; user: { id: string; name: string; email: string }; role: string }>
  const currentAccount = accounts.find((a) => a.id === currentAccountId)

  const [createOpen, setCreateOpen] = useState(false)
  const [createForm] = Form.useForm()
  const [editingProject, setEditingProject] = useState<any>(null)
  const [editForm] = Form.useForm()

  const [boardProjectId, setBoardProjectId] = useState<string | null>(null)
  const [boardOpen, setBoardOpen] = useState(false)
  const [boardForm] = Form.useForm()
  const [editingBoard, setEditingBoard] = useState<any>(null)
  const [editBoardForm] = Form.useForm()

  const handleCreateProject = async (values: any) => {
    try {
      await createProject({
        variables: {
          input: {
            name: values.name,
            description: values.description || null,
            icon: values.icon || null,
            memberIds: values.memberIds ?? [],
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

  const handleEditProject = async (values: any) => {
    if (!editingProject) return
    try {
      const isOwner = editingProject.ownerId === user?.id
      const isAdmin = accountMembers.some((m) => m.user.id === user?.id && m.role === 'Admin')
      const canEditMembers = isOwner || isAdmin

      const input: any = {
        projectId: editingProject.id,
        name: values.name,
        description: values.description || null,
        icon: values.icon || null,
      }
      if (canEditMembers) {
        input.memberIds = values.memberIds ?? []
      }

      await editProject({ variables: { input } })
      setEditingProject(null)
      editForm.resetFields()
      message.success('Project updated')
    } catch (err: unknown) {
      message.error(err instanceof Error ? err.message : 'Failed to update project')
    }
  }

  const handleDeleteProject = async (projectId: string) => {
    try {
      await deleteProject({ variables: { projectId } })
      message.success('Project deleted')
    } catch (err: unknown) {
      message.error(err instanceof Error ? err.message : 'Failed to delete project')
    }
  }

  const handleCreateBoard = async (values: { name: string; sections: string[]; memberIds?: string[] }) => {
    if (!boardProjectId) return
    try {
      await createBoard({
        variables: {
          input: {
            projectId: boardProjectId,
            name: values.name,
            sections: values.sections,
            memberIds: values.memberIds ?? [],
          },
        },
      })
      setBoardOpen(false)
      boardForm.resetFields()
      message.success('Board created')
    } catch (err: unknown) {
      message.error(err instanceof Error ? err.message : 'Failed to create board')
    }
  }

  const handleEditBoard = async (values: { name: string; memberIds?: string[] }) => {
    if (!editingBoard) return
    try {
      const isOwner = editingBoard.ownerId === user?.id
      const isAdmin = accountMembers.some((m) => m.user.id === user?.id && m.role === 'Admin')
      const canEditMembers = isOwner || isAdmin

      const input: any = {
        boardId: editingBoard.id,
        name: values.name,
      }
      if (canEditMembers) {
        input.memberIds = values.memberIds ?? []
      }

      await editBoard({ variables: { input } })
      setEditingBoard(null)
      editBoardForm.resetFields()
      message.success('Board updated')
    } catch (err: unknown) {
      message.error(err instanceof Error ? err.message : 'Failed to update board')
    }
  }

  const handleDeleteBoard = async (boardId: string) => {
    try {
      await deleteBoard({ variables: { boardId } })
      message.success('Board deleted')
    } catch (err: unknown) {
      message.error(err instanceof Error ? err.message : 'Failed to delete board')
    }
  }

  const pathMatch = (pattern: string) => {
    const path = location.pathname
    if (pattern === '/projects/:projectId') {
      const m = path.match(/^\/projects\/([^/]+)$/)
      return m ? m[1] : null
    }
    if (pattern === '/boards/:boardId') {
      const m = path.match(/^\/boards\/([^/]+)$/)
      return m ? m[1] : null
    }
    return null
  }

  const selectedProjectId = pathMatch('/projects/:projectId')
  const selectedBoardId = pathMatch('/boards/:boardId')
  const treeSelectedKey = selectedBoardId || selectedProjectId || undefined

  const treeData = projects.map((p: any) => ({
    key: p.id,
    title: (
      <ProjectNodeTitle
        project={p}
        onEdit={() => {
          setEditingProject(p)
          editForm.setFieldsValue({ name: p.name, description: p.description, icon: p.icon, memberIds: (p.members ?? []).map((m: any) => m.user.id) })
        }}
        onDelete={() => handleDeleteProject(p.id)}
        onCreateBoard={() => {
          setBoardProjectId(p.id)
          setBoardOpen(true)
          boardForm.setFieldsValue({ sections: ['To Do', 'In Progress', 'Done'] })
        }}
      />
    ),
    children: (p.boards ?? []).map((b: any) => ({
      key: b.id,
      icon: <ProjectOutlined style={{ color: 'rgba(255,255,255,0.45)', fontSize: 14 }} />,
      title: (
        <BoardNodeTitle
          name={b.name}
          canDelete={b.ownerId === user?.id || (b.members?.some((m: any) => m.user.id === user?.id) && accountMembers.some((m) => m.user.id === user?.id && m.role === 'Admin'))}
          onEdit={() => {
            setEditingBoard(b)
            editBoardForm.setFieldsValue({ name: b.name, memberIds: (b.members ?? []).map((m: any) => m.user.id) })
          }}
          onDelete={() => handleDeleteBoard(b.id)}
        />
      ),
    })),
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
                  selectedKeys={treeSelectedKey ? [treeSelectedKey] : []}
                  onSelect={(keys) => {
                    if (keys.length === 0) return
                    const key = keys[0] as string
                    const isBoard = projects.some((p: any) =>
                      (p.boards ?? []).some((b: any) => b.id === key)
                    )
                    if (isBoard) navigate(`/boards/${key}`)
                    else navigate(`/projects/${key}`)
                  }}
                  defaultExpandAll
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

      <Modal title="Create Project" open={createOpen} onCancel={() => { setCreateOpen(false); createForm.resetFields() }} onOk={() => createForm.submit()} okText="Create">
        <Form form={createForm} layout="vertical" onFinish={handleCreateProject} initialValues={{ memberIds: [] }}>
          <Form.Item name="name" label="Name" rules={[{ required: true, message: 'Enter a project name' }]}>
            <Input placeholder="Project name" />
          </Form.Item>
          <Form.Item name="description" label="Description">
            <Input.TextArea rows={3} placeholder="Optional description" />
          </Form.Item>
          <Form.Item name="icon" label="Icon">
            <IconPicker />
          </Form.Item>
          <Form.Item name="memberIds" label="Members">
            <Select
              mode="multiple"
              placeholder="Select members"
              options={accountMembers.map((m) => ({ value: m.user.id, label: `${m.user.name} (${m.user.email})` }))}
            />
          </Form.Item>
        </Form>
      </Modal>

      <Modal title="Edit Project" open={!!editingProject} onCancel={() => { setEditingProject(null); editForm.resetFields() }} onOk={() => editForm.submit()} okText="Save">
        <Form form={editForm} layout="vertical" onFinish={handleEditProject}>
          <Form.Item name="name" label="Name" rules={[{ required: true, message: 'Enter a project name' }]}>
            <Input placeholder="Project name" />
          </Form.Item>
          <Form.Item name="description" label="Description">
            <Input.TextArea rows={3} placeholder="Optional description" />
          </Form.Item>
          <Form.Item name="icon" label="Icon">
            <IconPicker />
          </Form.Item>
          {(() => {
            const isOwner = editingProject?.ownerId === user?.id
            const isAdmin = accountMembers.some((m) => m.user.id === user?.id && m.role === 'Admin')
            const canEditMembers = isOwner || isAdmin
            return (
              <Form.Item name="memberIds" label="Members">
                <Select
                  mode="multiple"
                  placeholder="Select members"
                  disabled={!canEditMembers}
                  options={accountMembers.map((m) => ({ value: m.user.id, label: `${m.user.name} (${m.user.email})` }))}
                />
              </Form.Item>
            )
          })()}
        </Form>
      </Modal>

      <Modal title="Create Board" open={boardOpen} onCancel={() => { setBoardOpen(false); boardForm.resetFields() }} onOk={() => boardForm.submit()} okText="Create">
        <Form form={boardForm} layout="vertical" onFinish={handleCreateBoard} initialValues={{ sections: ['To Do', 'In Progress', 'Done'] }}>
          <Form.Item name="name" label="Board name" rules={[{ required: true, message: 'Enter a board name' }]}>
            <Input placeholder="Board name" />
          </Form.Item>
          <Form.Item name="memberIds" label="Members">
            <Select
              mode="multiple"
              placeholder="Select project members"
              options={(() => {
                const project = projects.find((p: any) => p.id === boardProjectId)
                return (project?.members ?? []).map((m: any) => ({ value: m.user.id, label: `${m.user.name} (${m.user.email})` }))
              })()}
            />
          </Form.Item>
          <Form.List name="sections">
            {(fields, { add, remove }) => (
              <>
                {fields.map(({ key, name, ...rest }) => (
                  <div key={key} style={{ display: 'flex', gap: 8, marginBottom: 8, alignItems: 'center' }}>
                    <Form.Item {...rest} name={[name]} rules={[{ required: true, message: 'Required' }]} style={{ marginBottom: 0, flex: 1 }}>
                      <Input placeholder="Section name" />
                    </Form.Item>
                    <Button type="text" danger onClick={() => remove(name)} disabled={fields.length <= 1}>✕</Button>
                  </div>
                ))}
                <Button type="dashed" onClick={() => add()} block icon={<PlusOutlined />}>Add Section</Button>
              </>
            )}
          </Form.List>
        </Form>
      </Modal>

      <Modal title="Edit Board" open={!!editingBoard} onCancel={() => { setEditingBoard(null); editBoardForm.resetFields() }} onOk={() => editBoardForm.submit()} okText="Save">
        <Form form={editBoardForm} layout="vertical" onFinish={handleEditBoard}>
          <Form.Item name="name" label="Board name" rules={[{ required: true, message: 'Enter a board name' }]}>
            <Input placeholder="Board name" />
          </Form.Item>
          {(() => {
            const isOwner = editingBoard?.ownerId === user?.id
            const isBoardMember = editingBoard?.members?.some((m: any) => m.user.id === user?.id)
            const isAdmin = accountMembers.some((m) => m.user.id === user?.id && m.role === 'Admin')
            const canEditMembers = isOwner || (isBoardMember && isAdmin)
            return (
              <Form.Item name="memberIds" label="Members">
                <Select
                  mode="multiple"
                  placeholder="Select project members"
                  disabled={!canEditMembers}
                  options={(() => {
                    const project = projects.find((p: any) => p.id === editingBoard?.projectId)
                    return (project?.members ?? []).map((m: any) => ({ value: m.user.id, label: `${m.user.name} (${m.user.email})` }))
                  })()}
                />
              </Form.Item>
            )
          })()}
        </Form>
      </Modal>
    </Layout>
  )
}
