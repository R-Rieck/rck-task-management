import { gql } from '@apollo/client'

export const GET_PROJECTS = gql`
  query Projects {
    projects {
      id
      name
      description
      icon
      members {
        id
        user {
          id
          name
          email
        }
        joinedAt
      }
      boards {
        id
        name
        sections {
          id
          name
          position
        }
      }
      createdAt
      updatedAt
    }
  }
`

export const CREATE_PROJECT = gql`
  mutation CreateProject($input: CreateProjectInput!) {
    createProject(input: $input) {
      id
      name
      description
      icon
      members {
        id
        user {
          id
          name
          email
        }
        joinedAt
      }
      createdAt
      updatedAt
    }
  }
`

export const EDIT_PROJECT = gql`
  mutation EditProject($input: EditProjectInput!) {
    editProject(input: $input) {
      id
      name
      description
      icon
      members {
        id
        user {
          id
          name
          email
        }
        joinedAt
      }
      createdAt
      updatedAt
    }
  }
`

export const DELETE_PROJECT = gql`
  mutation DeleteProject($projectId: ProjectId!) {
    deleteProject(projectId: $projectId)
  }
`
