import { useContext } from 'react'
import RateContext from 'app/contexts/RateContext'

const useRate= () => useContext(RateContext)

export default useRate
