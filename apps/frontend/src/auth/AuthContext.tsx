import { createContext, useContext, useState, useEffect, useCallback, type ReactNode } from 'react'
import { useMutation } from '@apollo/client/react'
import { client } from '../apollo'
import { LOGIN, REGISTER, ME } from '../graphql/auth'
import { SWITCH_ACCOUNT } from '../graphql/account'

export interface User {
  id: string
  name: string
  email: string
}

interface AuthContextValue {
  user: User | null
  loading: boolean
  currentAccountId: string | null
  login: (email: string, password: string) => Promise<void>
  register: (name: string, email: string, password: string) => Promise<void>
  switchAccount: (accountId: string) => Promise<void>
  updateUser: (user: User) => void
  logout: () => void
}

const AuthContext = createContext<AuthContextValue | null>(null)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null)
  const [loading, setLoading] = useState(true)
  const [currentAccountId, setCurrentAccountId] = useState<string | null>(
    () => localStorage.getItem('currentAccountId'),
  )
  const [loginMutation] = useMutation(LOGIN)
  const [registerMutation] = useMutation(REGISTER)
  const [switchAccountMutation] = useMutation(SWITCH_ACCOUNT)

  useEffect(() => {
    const token = localStorage.getItem('accessToken')
    if (!token) {
      setLoading(false)
      return
    }
    client.query({ query: ME, fetchPolicy: 'network-only' })
      .then((result) => {
        setUser(result.data.me)
        setLoading(false)
      })
      .catch(() => {
        localStorage.removeItem('accessToken')
        localStorage.removeItem('refreshToken')
        localStorage.removeItem('currentAccountId')
        setLoading(false)
      })
  }, [])

  const login = useCallback(async (email: string, password: string) => {
    const result = await loginMutation({
      variables: { input: { email, password } },
    })
    const { accessToken, refreshToken, accountId } = result.data.login
    localStorage.setItem('accessToken', accessToken)
    localStorage.setItem('refreshToken', refreshToken)
    localStorage.setItem('currentAccountId', accountId)
    setCurrentAccountId(accountId)

    const meResult = await client.query({ query: ME, fetchPolicy: 'network-only' })
    setUser(meResult.data.me)
  }, [loginMutation])

  const register = useCallback(async (name: string, email: string, password: string) => {
    const result = await registerMutation({
      variables: { input: { name, email, password } },
    })
    const { accessToken, refreshToken, accountId } = result.data.register
    localStorage.setItem('accessToken', accessToken)
    localStorage.setItem('refreshToken', refreshToken)
    localStorage.setItem('currentAccountId', accountId)
    setCurrentAccountId(accountId)

    const meResult = await client.query({ query: ME, fetchPolicy: 'network-only' })
    setUser(meResult.data.me)
  }, [registerMutation])

  const updateUser = useCallback((userData: User) => {
    setUser(userData)
  }, [])

  const switchAccount = useCallback(async (accountId: string) => {
    const result = await switchAccountMutation({
      variables: { toAccountId: accountId },
    })
    const { accessToken, refreshToken } = result.data.switchAccount
    localStorage.setItem('accessToken', accessToken)
    localStorage.setItem('refreshToken', refreshToken)
    localStorage.setItem('currentAccountId', accountId)

    window.location.reload()
  }, [switchAccountMutation])

  const logout = useCallback(() => {
    localStorage.removeItem('accessToken')
    localStorage.removeItem('refreshToken')
    localStorage.removeItem('currentAccountId')
    setUser(null)
    setCurrentAccountId(null)
    client.resetStore()
  }, [])

  return (
    <AuthContext.Provider value={{ user, loading, currentAccountId, login, register, switchAccount, updateUser, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth(): AuthContextValue {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used within AuthProvider')
  return ctx
}
