import { gql } from '@apollo/client'

export const GET_BOARDS = gql`
  query Boards($projectId: ProjectId!) {
    boards(projectId: $projectId) {
      id
      name
      sections {
        id
        name
        position
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
      sections {
        id
        name
        position
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
      sections {
        id
        name
        position
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
