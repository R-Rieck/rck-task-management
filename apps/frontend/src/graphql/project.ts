import { gql } from '@apollo/client'

export const GET_PROJECTS = gql`
  query Projects {
    projects {
      id
      name
      description
      isPrivate
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
      isPrivate
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
      isPrivate
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
