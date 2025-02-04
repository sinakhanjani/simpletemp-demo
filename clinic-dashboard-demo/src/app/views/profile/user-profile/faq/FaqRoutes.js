import Loadable from 'app/components/Loadable';
import { lazy } from 'react';
import { authRoles } from '../../../../auth/authRoles';

const FaqList = Loadable(lazy(() => import('./FaqList')));

const faqRoutes = [
    { path: '/clinic/faq', element: <FaqList />, auth: authRoles.admin },
];

export default faqRoutes;
