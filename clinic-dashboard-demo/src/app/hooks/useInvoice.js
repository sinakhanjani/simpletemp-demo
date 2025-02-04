import { useContext } from 'react'
import InvoiceContext from 'app/contexts/InvoiceContext'

const useInvoice = () => useContext(InvoiceContext)

export default useInvoice
