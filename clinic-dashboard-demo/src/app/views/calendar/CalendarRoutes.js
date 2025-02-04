import Loadable from 'app/components/Loadable';
import { lazy } from 'react';
import { authRoles } from '../../auth/authRoles';

const ShiftsCalendar = Loadable(lazy(() => import('./Calendar')));
const CreateShift = Loadable(lazy(() => import('./CreateShift')));
const OfferOnShift = Loadable(lazy(() => import('./OfferOnShift')));


const calendarRoutes = [
    { path: '/clinic/calendar', element: <ShiftsCalendar />, auth: authRoles.admin },
    { path: '/clinic/calendar/:shiftId/', element: <OfferOnShift />, auth: authRoles.admin },
    { path: '/clinic/calendar/post-shift', element: <CreateShift />, auth: authRoles.admin },
];

export default calendarRoutes;
