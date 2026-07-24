import { gql } from '@apollo/client'

export const GET_BOARDS = gql`
  query Boards($projectId: ProjectId!) {
    boards(projectId: $projectId) {
      id
      name
      ownerId
      sections {
        id
        name
        position
      }
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

export const GET_BOARD = gql`
  query Board($id: BoardId!) {
    board(id: $id) {
      id
      name
      ownerId
      sections {
        id
        name
        position
      }
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

export const CREATE_BOARD = gql`
  mutation CreateBoard($input: CreateBoardInput!) {
    createBoard(input: $input) {
      id
      name
      ownerId
      sections {
        id
        name
        position
      }
      members {
        id
        user {
          id
          name
          email
        }
        joinedAt
      }
    }
  }
`

export const EDIT_BOARD = gql`
  mutation EditBoard($input: EditBoardInput!) {
    editBoard(input: $input) {
      id
      name
      ownerId
      sections {
        id
        name
        position
      }
      members {
        id
        user {
          id
          name
          email
        }
        joinedAt
      }
    }
  }
`

export const DELETE_BOARD = gql`
  mutation DeleteBoard($boardId: BoardId!) {
    deleteBoard(boardId: $boardId)
  }
`

export const CREATE_BOARD_SECTION = gql`
  mutation CreateBoardSection($input: CreateBoardSectionInput!) {
    createBoardSection(input: $input) {
      id
      name
      position
    }
  }
`

export const DELETE_BOARD_SECTION = gql`
  mutation DeleteBoardSection($sectionId: BoardSectionId!) {
    deleteBoardSection(sectionId: $sectionId)
  }
`

export const RENAME_BOARD_SECTION = gql`
  mutation RenameBoardSection($sectionId: BoardSectionId!, $name: String!) {
    renameBoardSection(sectionId: $sectionId, name: $name) {
      id
      name
      position
    }
  }
`
