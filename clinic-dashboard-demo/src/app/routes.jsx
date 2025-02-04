import AuthGuard from 'app/auth/AuthGuard';
import chartsRoute from 'app/views/charts/ChartsRoute';
import dashboardRoutes from 'app/views/dashboard/DashboardRoutes';
import materialRoutes from 'app/views/material-kit/MaterialRoutes';
import notificationsRoutes from './views/notifications/NotificationRoutes';
import settingsRoutes from './views/profile/setting/SettingRoutes';
import profileRoutes from './views/profile/user-profile/ProfileRoutes';
import NotFound from 'app/views/sessions/NotFound';
import sessionRoutes from 'app/views/sessions/SessionRoutes';
import { Navigate } from 'react-router-dom';
import MatxLayout from './components/MatxLayout/MatxLayout';
import supportRoutes from './views/profile/user-profile/support/SupportRoutes';
import faqRoutes from './views/profile/user-profile/faq/FaqRoutes';
import calendarRoutes from './views/calendar/CalendarRoutes';
import invoiceRoutes from './views/Invoice/InvoiceRoutes';

const routes = [
  {
    element: (
      <AuthGuard>
        <MatxLayout />
      </AuthGuard>
    ),
    children: [
      ...dashboardRoutes,
      ...chartsRoute,
      ...materialRoutes,
      ...notificationsRoutes,
      ...settingsRoutes,
      ...profileRoutes,
      ...supportRoutes,
      ...faqRoutes,
      ...calendarRoutes,
      ...invoiceRoutes,
    ],
  },
  ...sessionRoutes,
  { path: '/clinic', element: <Navigate to="dashboard" /> },
  { path: '*', element: <NotFound /> },
];

export default routes;
