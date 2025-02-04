import Loadable from 'app/components/Loadable';
import { lazy } from 'react';
import { authRoles } from '../../../auth/authRoles';

const UserProfile = Loadable(lazy(() => import('./UserProfile')));
const NoteAndDocument = Loadable(lazy(() => import('./note-and-docs/note-docs')));
const ClinicInformation = Loadable(lazy(() => import('./profile/account-information/clinic-information/ClinicInformation')));
const PinLocation = Loadable(lazy(() => import('./profile/account-information/pin-location/PinLocation')));
const ClinicDetail = Loadable(lazy(() => import('./profile/clinic-details/ClinicDetail')));
const ManagePayment = Loadable(lazy(() => import('./profile/manage-payment/ManagePayment')));
const Rates = Loadable(lazy(() => import('./profile/rates/Rates')));
const MyProfile = Loadable(lazy(() => import('./profile/myProfile/MyProfile')));

const profileRoutes = [
    { path: '/clinic/user-profile', element: <UserProfile />, auth: authRoles.admin },
    { path: '/clinic/user-profile/my-profile', element: <MyProfile />, auth: authRoles.admin },
    { path: '/clinic/user-profile/note-and-docs', element: <NoteAndDocument />, auth: authRoles.admin },
    // { path: '/clinic/user-profile/clinic-information', element: <ClinicInformation />, auth: authRoles.admin },
    { path: '/clinic/user-profile/my-profile/pin-location', element: <PinLocation />, auth: authRoles.admin },
    // { path: '/clinic/user-profile/clinic-detail', element: <ClinicDetail />, auth: authRoles.admin },
    // { path: '/clinic/user-profile/manage-payment', element: <ManagePayment />, auth: authRoles.admin },
    { path: '/clinic/user-profile/rates', element: <Rates />, auth: authRoles.admin },
];

export default profileRoutes;
