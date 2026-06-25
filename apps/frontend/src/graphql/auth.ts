import { gql } from '@apollo/client'

export const LOGIN = gql`
  mutation Login($input: LoginUserInput!) {
    login(input: $input) {
      accessToken
      refreshToken
      userId
      accountId
    }
  }
`

export const REGISTER = gql`
  mutation Register($input: RegisterUserInput!) {
    register(input: $input) {
      accessToken
      refreshToken
      userId
      accountId
    }
  }
`

export const ACCEPT_INVITE_NEW_USER = gql`
  mutation AcceptInviteWithNewUser($invitationToken: ID, $name: String, $email: String, $password: String) {
    acceptInviteWithNewUser(invitationToken: $invitationToken, name: $name, email: $email, password: $password) {
      accessToken
      refreshToken
      userId
      accountId
    }
  }
`

export const ACCEPT_INVITE_EXISTING_USER = gql`
  mutation AcceptInviteWithExistingUser($invitationToken: ID, $userId: UserId) {
    acceptInviteWithExistingUser(invitationToken: $invitationToken, userId: $userId) {
      accessToken
      refreshToken
      userId
      accountId
    }
  }
`

export const EDIT_USER = gql`
  mutation EditUser($input: EditUserInput!) {
    editUser(input: $input) {
      id
      name
      email
    }
  }
`

export const REFRESH = gql`
  mutation Refresh($input: RefreshAuthenticationInput!) {
    refresh(input: $input) {
      accessToken
      refreshToken
      userId
      accountId
    }
  }
`

export const ME = gql`
  query Me {
    me {
      id
      name
      email
    }
  }
`
