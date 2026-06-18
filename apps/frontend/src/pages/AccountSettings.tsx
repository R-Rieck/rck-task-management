import { useState } from 'react'
import { useQuery, useMutation } from '@apollo/client/react'
import {
  Typography,
  Card,
  Table,
  Tag,
  Select,
  Button,
  Form,
  Input,
  message,
  Spin,
  Alert,
  Popconfirm,
} from 'antd'
import { MailOutlined, UserAddOutlined, DeleteOutlined } from '@ant-design/icons'
import { GET_MEMBERS, INVITE, EDIT_MEMBER_ROLE, REMOVE_MEMBER } from '../graphql/account'
import { AppLayout } from '../components/AppLayout'
import { useAuth } from '../auth/AuthContext'

const { Title, Text } = Typography

export function AccountSettings() {
  const { user, logout } = useAuth()
  const { data, loading, error } = useQuery(GET_MEMBERS)
  const [inviteMutation, { loading: inviting }] = useMutation(INVITE)
  const [editRoleMutation] = useMutation(EDIT_MEMBER_ROLE)
  const [removeMutation] = useMutation(REMOVE_MEMBER)
  const [inviteForm] = Form.useForm()
  const [members, setMembers] = useState<Member[]>([])
  const [invitations, setInvitations] = useState<Invitation[]>([])
  const [accountName, setAccountName] = useState('')

  if (data && members.length === 0 && data.getMembers) {
    setMembers(data.getMembers.members)
    setInvitations(data.getMembers.openInvitations)
    setAccountName(data.getMembers.accountName)
  }

  const handleInvite = async (values: { email: string }) => {
    try {
      const result = await inviteMutation({
        variables: { emails: [values.email] },
      })
      const updated = result.data.invite
      setMembers(updated.members)
      setInvitations(updated.openInvitations)
      setAccountName(updated.accountName)
      inviteForm.resetFields()
      message.success('Invitation sent')
    } catch (err: unknown) {
      const msg = err instanceof Error ? err.message : 'Failed to send invitation'
      message.error(msg)
    }
  }

  const handleRoleChange = async (userId: string, newRole: string) => {
    try {
      const result = await editRoleMutation({
        variables: { userId, role: newRole },
      })
      const updated = result.data.editAccountMemberRole
      setMembers((prev) =>
        prev.map((m) =>
          m.user.id === updated.user.id ? { ...m, role: updated.role } : m,
        ),
      )
      message.success('Role updated')
    } catch (err: unknown) {
      const msg = err instanceof Error ? err.message : 'Failed to update role'
      message.error(msg)
    }
  }

  const handleRemoveMember = async (userId: string) => {
    try {
      await removeMutation({ variables: { userId } })
      const isSelf = userId === user?.id
      if (isSelf) {
        logout()
        return
      }
      setMembers((prev) => prev.filter((m) => m.user.id !== userId))
      message.success('Member removed')
    } catch (err: unknown) {
      const msg = err instanceof Error ? err.message : 'Failed to remove member'
      message.error(msg)
    }
  }

  if (loading) {
    return (
      <AppLayout>
        <div style={{ display: 'flex', justifyContent: 'center', paddingTop: 56 }}>
          <Spin size="large" />
        </div>
      </AppLayout>
    )
  }

  if (error) {
    return (
      <AppLayout>
        <Alert type="error" message="Failed to load account settings" description={error.message} />
      </AppLayout>
    )
  }

  const isSelf = (member: Member) => member.user.id === user?.id
  const currentUserRole = members.find((m) => m.user.id === user?.id)?.role

  const memberColumns = [
    {
      title: 'Name',
      dataIndex: ['user', 'name'],
      key: 'name',
    },
    {
      title: 'Email',
      dataIndex: ['user', 'email'],
      key: 'email',
    },
    {
      title: 'Role',
      key: 'role',
      render: (_: unknown, record: Member) => {
        if (currentUserRole !== 'Admin') return <span>{record.role}</span>
        return (
          <Select
            value={record.role}
            style={{ width: 100 }}
            onChange={(value: string) => handleRoleChange(record.user.id, value)}
            options={[
              { value: 'User', label: 'User' },
              { value: 'Admin', label: 'Admin' },
            ]}
          />
        )
      },
    },
    {
      title: 'Actions',
      key: 'actions',
      render: (_: unknown, record: Member) => {
        if (isSelf(record)) {
          return (
            <Popconfirm
              title="Leave this account?"
              description="You will be signed out."
              onConfirm={() => handleRemoveMember(record.user.id)}
              okText="Leave"
              okButtonProps={{ danger: true }}
            >
              <Button type="text" danger icon={<DeleteOutlined />} size="small">
                Leave
              </Button>
            </Popconfirm>
          )
        }
        if (currentUserRole !== 'Admin') return null
        return (
          <Popconfirm
            title="Remove this member?"
            onConfirm={() => handleRemoveMember(record.user.id)}
            okText="Remove"
            okButtonProps={{ danger: true }}
          >
            <Button type="text" danger icon={<DeleteOutlined />} size="small">
              Remove
            </Button>
          </Popconfirm>
        )
      },
    },
  ]

  const invitationColumns = [
    {
      title: 'Email',
      dataIndex: 'inviteeEmail',
      key: 'inviteeEmail',
    },
    {
      title: 'Expires',
      dataIndex: 'expirationDate',
      key: 'expirationDate',
      render: (date: string) => {
        const expires = new Date(date)
        const now = new Date()
        const diff = expires.getTime() - now.getTime()
        if (diff <= 0) return <Tag color="red">Expired</Tag>
        const days = Math.floor(diff / (1000 * 60 * 60 * 24))
        const hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60))
        return <span>{days}d {hours}h remaining</span>
      },
    },
  ]

  return (
    <AppLayout>
      <Title level={2}>Account Settings</Title>
      <Text type="secondary" style={{ display: 'block', marginBottom: 24 }}>
        Account: {accountName}
      </Text>

      <Card title="Members" style={{ marginBottom: 24 }}>
        <Table
          dataSource={members}
          columns={memberColumns}
          rowKey="id"
          pagination={false}
        />
      </Card>

      <Card title="Invite Members" style={{ marginBottom: 24 }}>
        <Form form={inviteForm} layout="inline" onFinish={handleInvite}>
          <Form.Item
            name="email"
            rules={[
              { required: true, message: 'Enter an email address' },
              { type: 'email', message: 'Enter a valid email' },
            ]}
            style={{ flex: 1 }}
          >
            <Input
              prefix={<MailOutlined />}
              placeholder="email@example.com"
              size="large"
            />
          </Form.Item>
          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              icon={<UserAddOutlined />}
              loading={inviting}
              size="large"
            >
              Send Invite
            </Button>
          </Form.Item>
        </Form>
      </Card>

      {invitations.length > 0 && (
        <Card title="Pending Invitations">
          <Table
            dataSource={invitations}
            columns={invitationColumns}
            rowKey="id"
            pagination={false}
          />
        </Card>
      )}
    </AppLayout>
  )
}

interface Member {
  id: string
  user: { id: string; name: string; email: string }
  role: string
}

interface Invitation {
  id: string
  inviteeEmail: string
  expirationDate: string
  invitedByUserId: string
}
