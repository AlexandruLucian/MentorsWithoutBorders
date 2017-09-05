// ------------------------------------
// Constants
// ------------------------------------
export const SHOW_ALL = 'show_all'
export const SHOW_COMPLETED = 'show_completed'
export const SHOW_ACTIVE = 'show_active'
export const ADD_STEP = 'ADD_STEP'
export const DELETE_STEP = 'DELETE_STEP'
export const EDIT_STEP = 'EDIT_STEP'
export const COMPLETE_STEP = 'COMPLETE_STEP'
export const COMPLETE_ALL = 'COMPLETE_ALL'
export const CLEAR_COMPLETED = 'CLEAR_COMPLETED'

// ------------------------------------
// Actions
// ------------------------------------
export function addStep(text) {
  return { 
    type: ADD_STEP, 
    text: text 
  }
} 
export function deleteStep(id) { 
  return {
    type: DELETE_STEP, 
    id: id 
  }  
}  
export function editStep(id) { 
  return {
    type: EDIT_STEP, 
    id: id,
    text: text 
  }  
}
export function completeStep(id) { 
  return {
    type: COMPLETE_STEP, 
    id: id
  }  
}
export function completeAll(id) { 
  return {
    type: COMPLETE_ALL, 
    id: id
  }  
}  
export function clearCompleted(id) { 
  return {
    type: CLEAR_COMPLETED
  }  
} 
export const actions = {
  addStep,
  deleteStep,
  editStep,
  completeStep,
  completeAll,
  clearCompleted
}

// ------------------------------------
// Reducer
// ------------------------------------
const initialState = [
  {
    text: 'Use Redux',
    completed: false,
    id: 0
  }
]

export default function steps(state = initialState, action) {
  switch (action.type) {
    case ADD_STEP:
      return [
        {
          id: state.reduce((maxId, step) => Math.max(step.id, maxId), -1) + 1,
          completed: false,
          text: action.text
        },
        ...state
      ]

    case DELETE_STEP:
      return state.filter(step =>
        step.id !== action.id
      )

    case EDIT_STEP:
      return state.map(step =>
        step.id === action.id ?
          { ...step, text: action.text } :
          step
      )

    case COMPLETE_STEP:
      return state.map(step =>
        step.id === action.id ?
          { ...step, completed: !step.completed } :
          step
      )

    case COMPLETE_ALL:
      const areAllMarked = state.every(step => step.completed)
      return state.map(step => ({
        ...step,
        completed: !areAllMarked
      }))

    case CLEAR_COMPLETED:
      return state.filter(step => step.completed === false)

    default:
      return state
  }
}
