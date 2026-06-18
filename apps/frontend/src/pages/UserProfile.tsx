import { useState } from 'react'
import { Typography, Card, Form, Input, Button, message, Space, Divider } from 'antd'
import { useMutation } from '@apollo/client/react'
import { MailOutlined, LockOutlined, UserOutlined } from '@ant-design/icons'
import { AppLayout } from '../components/AppLayout'
import { useAuth } from '../auth/AuthContext'
import { EDIT_USER } from '../graphql/auth'

const { Title } = Typography

export function UserProfile() {
  const { user, updateUser } = useAuth()
  const [form] = Form.useForm()
  const [saving, setSaving] = useState(false)
  const [editUserMutation] = useMutation(EDIT_USER)

  const handleSave = async (values: { name: string; email: string; password?: string; confirmPassword?: string }) => {
    setSaving(true)
    try {
      const input: Record<string, string> = { name: values.name, email: values.email }
      if (values.password) {
        input.password = values.password
      }

      const result = await editUserMutation({ variables: { input } })
      updateUser(result.data.editUser)
      form.resetFields(['password', 'confirmPassword'])
      message.success('Profile updated')
    } catch (err: unknown) {
      const msg = err instanceof Error ? err.message : 'Failed to update profile'
      message.error(msg)
    } finally {
      setSaving(false)
    }
  }

  return (
    <AppLayout>
      <Title level={2}>Profile</Title>

      <Card title="Account info" style={{ maxWidth: 500, marginBottom: 24 }}>
        <Form
          form={form}
          layout="vertical"
          onFinish={handleSave}
          initialValues={{ name: user?.name, email: user?.email }}
        >
          <Form.Item
            name="name"
            label="Name"
            rules={[{ required: true, message: 'Enter your name' }]}
          >
            <Input prefix={<UserOutlined />} size="large" />
          </Form.Item>

          <Form.Item
            name="email"
            label="Email"
            rules={[
              { required: true, message: 'Enter your email' },
              { type: 'email', message: 'Enter a valid email' },
            ]}
          >
            <Input prefix={<MailOutlined />} size="large" />
          </Form.Item>

          <Divider />

          <Title level={5}>Change password</Title>
          <Form.Item
            name="password"
            label="New password"
            rules={[
              { min: 8, message: 'At least 8 characters' },
            ]}
          >
            <Input.Password prefix={<LockOutlined />} placeholder="Leave blank to keep current" size="large" />
          </Form.Item>

          <Form.Item
            name="confirmPassword"
            label="Confirm new password"
            dependencies={['password']}
            rules={[
              ({ getFieldValue }) => ({
                validator(_, value) {
                  const password = getFieldValue('password')
                  if (!password) return Promise.resolve()
                  if (!value) return Promise.reject(new Error('Confirm your password'))
                  if (value !== password) return Promise.reject(new Error('Passwords do not match'))
                  return Promise.resolve()
                },
              }),
            ]}
          >
            <Input.Password prefix={<LockOutlined />} placeholder="Re-enter new password" size="large" />
          </Form.Item>

          <Form.Item style={{ marginBottom: 0, marginTop: 16 }}>
            <Button type="primary" htmlType="submit" loading={saving} size="large">
              Save Changes
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </AppLayout>
  )
}
