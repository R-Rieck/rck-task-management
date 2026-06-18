import { Avatar, Dropdown } from 'antd'
import { UserOutlined, TeamOutlined, LogoutOutlined, CheckOutlined, SwapOutlined } from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'
import { useQuery } from '@apollo/client/react'
import { useAuth } from '../auth/AuthContext'
import { GET_USER_ACCOUNTS } from '../graphql/account'

export function UserMenu() {
  const { user, currentAccountId, switchAccount, logout } = useAuth()
  const { data: accountsData } = useQuery(GET_USER_ACCOUNTS)
  const navigate = useNavigate()

  const initials = user?.name
    .split(' ')
    .map((part) => part[0])
    .join('')
    .toUpperCase()
    .slice(0, 2)

  const accounts = accountsData?.getUserAccounts ?? []

  const items = [
    { key: 'profile', icon: <UserOutlined />, label: 'Profile' },
    { key: 'account', icon: <TeamOutlined />, label: 'Account settings' },
    { type: 'divider' as const },
    ...(accounts.length > 0
      ? [
          {
            key: 'switch-sub',
            icon: <SwapOutlined />,
            label: 'Switch account',
            children: accounts.map((acct: { id: string; name: string }) => ({
              key: `switch_${acct.id}`,
              icon: acct.id === currentAccountId ? <CheckOutlined /> : undefined,
              label: acct.name,
              disabled: acct.id === currentAccountId,
            })),
          },
        ]
      : []),
    { type: 'divider' as const },
    { key: 'logout', icon: <LogoutOutlined />, label: 'Sign Out', danger: true },
  ]

  const handleMenuClick = ({ key }: { key: string }) => {
    if (key === 'logout') {
      logout()
      navigate('/login')
    } else if (key.startsWith('switch_')) {
      const accountId = key.replace('switch_', '')
      switchAccount(accountId)
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
