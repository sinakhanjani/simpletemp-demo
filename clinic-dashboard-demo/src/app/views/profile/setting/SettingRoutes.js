import Loadable from 'app/components/Loadable';
import { lazy } from 'react';
import { authRoles } from '../../../auth/authRoles';

const Setting = Loadable(lazy(() => import('./Setting')));

const settingsRoutes = [
    { path: '/clinic/setting', element: <Setting />, auth: authRoles.admin },
];

export default settingsRoutes;
