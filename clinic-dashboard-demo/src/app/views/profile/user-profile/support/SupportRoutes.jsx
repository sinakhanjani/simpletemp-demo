import Loadable from 'app/components/Loadable';
import { lazy } from 'react';
import { authRoles } from '../../../../auth/authRoles';

const TicketList = Loadable(lazy(() => import('./TicketList')))
const AddTicket = Loadable(lazy(() => import('./AddTicket')))
const Messages = Loadable(lazy(() => import('./Messages')))

const supportRoutes = [
    {
        path: '/clinic/ticket-list',
        element: <TicketList />,
        auth: authRoles.admin,
    },
    {
        path: '/clinic/send-ticket',
        element: <AddTicket />,
        auth: authRoles.admin,
    },
    {
        path: '/clinic/ticket-list/:ticketId',
        element: <Messages />,
        auth: authRoles.admin,
    },
]

export default supportRoutes