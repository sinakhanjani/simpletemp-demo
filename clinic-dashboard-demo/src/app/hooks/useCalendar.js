import { useContext } from 'react'
import CalendarContext from 'app/contexts/CalendarContext'

const useCalendar = () => useContext(CalendarContext)

export default useCalendar
