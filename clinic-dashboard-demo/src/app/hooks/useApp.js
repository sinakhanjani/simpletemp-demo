import { useContext } from 'react'
import AppContext from 'app/contexts/AppContext'

const useApp = () => useContext(AppContext)

export default useApp
