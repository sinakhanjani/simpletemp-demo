import { useContext } from 'react'
import ProfileContext from 'app/contexts/ProfileContext'

const useProfile = () => useContext(ProfileContext)

export default useProfile
