import { useState } from 'react'
import { useParams, useNavigate, Link } from 'react-router-dom'
import { useMutation } from '@apollo/client/react'
import { Card, Typography, message, Tabs, Form, Input, Button, Space } from 'antd'
import { MailOutlined, LockOutlined, UserOutlined } from '@ant-design/icons'
import { useAuth } from '../auth/AuthContext'
import { client } from '../apollo'
import { LOGIN, ACCEPT_INVITE_NEW_USER, ACCEPT_INVITE_EXISTING_USER, ME } from '../graphql/auth'

const { Title, Text } = Typography

export function AcceptInvite() {
  const { token } = useParams<{ token: string }>()
  const navigate = useNavigate()
  const { user, loading: authLoading } = useAuth()
  const [newUserForm] = Form.useForm()
  const [existingUserForm] = Form.useForm()
  const [submitting, setSubmitting] = useState(false)
  const [tab, setTab] = useState('create')
  const [acceptNew] = useMutation(ACCEPT_INVITE_NEW_USER)
  const [acceptExisting] = useMutation(ACCEPT_INVITE_EXISTING_USER)
  const [loginMutation] = useMutation(LOGIN)

  if (authLoading) return null
  if (user) {
    navigate('/', { replace: true })
    return null
  }

  const handleCreateAndJoin = async (values: { name: string; email: string; password: string }) => {
    if (!token) return
    setSubmitting(true)
    try {
      const result = await acceptNew({
        variables: {
          invitationToken: token,
          name: values.name,
          email: values.email,
          password: values.password,
        },
      })
      const { accessToken, refreshToken } = result.data.acceptInviteWithNewUser
      localStorage.setItem('accessToken', accessToken)
      localStorage.setItem('refreshToken', refreshToken)
      await client.query({ query: ME, fetchPolicy: 'network-only' })
      navigate('/', { replace: true })
    } catch (err: unknown) {
      const msg = err instanceof Error ? err.message : 'Failed to accept invitation'
      message.error(msg)
    } finally {
      setSubmitting(false)
    }
  }

  const handleSignInAndJoin = async (values: { email: string; password: string }) => {
    if (!token) return
    setSubmitting(true)
    try {
      const loginResult = await loginMutation({
        variables: { input: { email: values.email, password: values.password } },
      })
      const { accessToken, refreshToken, userId } = loginResult.data.login
      localStorage.setItem('accessToken', accessToken)
      localStorage.setItem('refreshToken', refreshToken)

      const acceptResult = await acceptExisting({
        variables: { invitationToken: token, userId },
      })
      const newTokens = acceptResult.data.acceptInviteWithExistingUser
      localStorage.setItem('accessToken', newTokens.accessToken)
      localStorage.setItem('refreshToken', newTokens.refreshToken)

      await client.query({ query: ME, fetchPolicy: 'network-only' })
      navigate('/', { replace: true })
    } catch (err: unknown) {
      const msg = err instanceof Error ? err.message : 'Failed to accept invitation'
      message.error(msg)
    } finally {
      setSubmitting(false)
    }
  }

  const tabItems = [
    {
      key: 'create',
      label: 'Create Account',
      children: (
        <Form form={newUserForm} layout="vertical" onFinish={handleCreateAndJoin} autoComplete="off">
          <Form.Item name="name" label="Name" rules={[{ required: true, message: 'Enter your name' }]}>
            <Input prefix={<UserOutlined />} placeholder="Your name" size="large" />
          </Form.Item>
          <Form.Item
            name="email"
            label="Email"
            rules={[
              { required: true, message: 'Enter your email' },
              { type: 'email', message: 'Enter a valid email' },
            ]}
          >
            <Input prefix={<MailOutlined />} placeholder="you@example.com" size="large" />
          </Form.Item>
          <Form.Item
            name="password"
            label="Password"
            rules={[
              { required: true, message: 'Enter a password' },
              { min: 8, message: 'At least 8 characters' },
            ]}
          >
            <Input.Password prefix={<LockOutlined />} placeholder="At least 8 characters" size="large" />
          </Form.Item>
          <Form.Item style={{ marginBottom: 0 }}>
            <Button type="primary" htmlType="submit" block size="large" loading={submitting}>
              Create Account & Join
            </Button>
          </Form.Item>
        </Form>
      ),
    },
    {
      key: 'signin',
      label: 'Sign In',
      children: (
        <Form form={existingUserForm} layout="vertical" onFinish={handleSignInAndJoin} autoComplete="off">
          <Form.Item
            name="email"
            label="Email"
            rules={[
              { required: true, message: 'Enter your email' },
              { type: 'email', message: 'Enter a valid email' },
            ]}
          >
            <Input prefix={<MailOutlined />} placeholder="you@example.com" size="large" />
          </Form.Item>
          <Form.Item name="password" label="Password" rules={[{ required: true, message: 'Enter your password' }]}>
            <Input.Password prefix={<LockOutlined />} placeholder="Password" size="large" />
          </Form.Item>
          <Form.Item style={{ marginBottom: 0 }}>
            <Button type="primary" htmlType="submit" block size="large" loading={submitting}>
              Sign In & Join
            </Button>
          </Form.Item>
        </Form>
      ),
    },
  ]

  return (
    <div
      style={{
        minHeight: '100vh',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        background: '#f0f2f5',
      }}
    >
      <Card style={{ width: 440 }}>
        <Space direction="vertical" size="middle" style={{ width: '100%' }}>
          <div style={{ textAlign: 'center' }}>
            <Title level={3}>Join Your Team</Title>
            <Text type="secondary">
              You've been invited to join a team. Create an account or sign in to accept.
            </Text>
          </div>

          <Tabs activeKey={tab} onChange={setTab} centered items={tabItems} />

          <div style={{ textAlign: 'center' }}>
            <Text>
              <Link to="/login">Back to Sign In</Link>
            </Text>
          </div>
        </Space>
      </Card>
    </div>
  )
}
