import { gql } from '@apollo/client'

export const GET_MEMBERS = gql`
  query GetMembers {
    getMembers {
      accountName
      members {
        id
        user {
          id
          name
          email
        }
        role
      }
      openInvitations {
        id
        inviteeEmail
        expirationDate
        invitedByUserId
      }
    }
  }
`

export const INVITE = gql`
  mutation Invite($emails: [String]) {
    invite(emails: $emails) {
      accountName
      members {
        id
        user {
          id
          name
          email
        }
        role
      }
      openInvitations {
        id
        inviteeEmail
        expirationDate
        invitedByUserId
      }
    }
  }
`

export const REMOVE_MEMBER = gql`
  mutation RemoveMember($userId: UserId!) {
    removeAccountMember(userId: $userId)
  }
`

export const EDIT_MEMBER_ROLE = gql`
  mutation EditMemberRole($userId: UserId!, $role: Role!) {
    editAccountMemberRole(userId: $userId, role: $role) {
      id
      user {
        id
        name
        email
      }
      role
    }
  }
`

export const GET_USER_ACCOUNTS = gql`
  query GetUserAccounts {
    getUserAccounts {
      id
      name
    }
  }
`

export const REMOVE_INVITATION = gql`
  mutation RemoveInvitation($invitationId: InvitationId!) {
    removeInvitation(invitationId: $invitationId)
  }
`

export const SWITCH_ACCOUNT = gql`
  mutation SwitchAccount($toAccountId: AccountId!) {
    switchAccount(toAccountId: $toAccountId) {
      accessToken
      refreshToken
      userId
      accountId
    }
  }
`
