import Loadable from 'app/components/Loadable';
import { lazy } from 'react';

const NotFound = Loadable(lazy(() => import('./NotFound')));
const ForgotPassword = Loadable(lazy(() => import('./ForgotPassword')));
const JwtLogin = Loadable(lazy(() => import('./JwtLogin')));
const JwtRegister = Loadable(lazy(() => import('./JwtRegister')));

const sessionRoutes = [
  { path: '/clinic/session/signup', element: <JwtRegister /> },
  { path: '/clinic/session/signin', element: <JwtLogin /> },
  { path: '/clinic/session/forgot-password', element: <ForgotPassword /> },
  { path: '/clinic/session/404', element: <NotFound /> },
];

export default sessionRoutes;
