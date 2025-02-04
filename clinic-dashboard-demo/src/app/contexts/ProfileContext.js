import React, { createContext, useEffect, useReducer } from 'react'

const initialState = {
    step: 'clinic-Information',
}

const ProfileContext = React.createContext({
    ...initialState,
    changeStep: () => { },
})

const reducer = (state, action) => {
    switch (action.type) {
        case 'POPULATE': {
            return action.payload
        }
        case 'STEP_CHANGED': {
            return {
                ...state,
                step: action.payload.step,
            }
        }
        case 'LOCATION_UPDATED': {
            return {
                ...state,
                location: action.payload.location,
            }
        }
        default: {
            return { ...state }
        }
    }
}

export const ProfileProvider = ({ children }) => {
    const [state, dispatch] = useReducer(reducer, initialState)

    const changeStep = (step) => {
        dispatch({
            type: 'STEP_CHANGED',
            payload: {
                step,
            },
        })
    }

    return (
        <ProfileContext.Provider
            value={{
                ...state,
                changeStep,
            }}
        >
            {children}
        </ProfileContext.Provider>
    )
}

export default ProfileContext
