import { Avatar, Dropdown } from 'antd'
import { UserOutlined, TeamOutlined, LogoutOutlined } from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

export function UserMenu() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  const initials = user?.name
    .split(' ')
    .map((part) => part[0])
    .join('')
    .toUpperCase()
    .slice(0, 2)

  const items = [
    { key: 'profile', icon: <UserOutlined />, label: 'Profile' },
    { key: 'account', icon: <TeamOutlined />, label: 'Account settings' },
    { type: 'divider' as const },
    { key: 'logout', icon: <LogoutOutlined />, label: 'Sign Out', danger: true },
  ]

  const handleMenuClick = ({ key }: { key: string }) => {
    if (key === 'logout') {
      logout()
      navigate('/login')
    } else {
      navigate(`/${key}`)
    }
  }

  return (
    <Dropdown
      menu={{ items, onClick: handleMenuClick, style: { minWidth: 200 } }}
      placement="bottomRight"
    >
      <div style={{ cursor: 'pointer', display: 'flex', alignItems: 'center', gap: 8 }}>
        <Avatar size="small" style={{ backgroundColor: '#1677ff', verticalAlign: 'middle' }}>
          {initials}
        </Avatar>
        <span style={{ color: 'rgba(255,255,255,0.85)' }}>{user?.name}</span>
      </div>
    </Dropdown>
  )
}
