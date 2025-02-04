import Loadable from 'app/components/Loadable';
import { lazy } from 'react';

const AppTable = Loadable(lazy(() => import('./tables/AppTable')));
const AppForm = Loadable(lazy(() => import('./forms/AppForm')));
const AppButton = Loadable(lazy(() => import('./buttons/AppButton')));
const AppIcon = Loadable(lazy(() => import('./icons/AppIcon')));
const AppProgress = Loadable(lazy(() => import('./AppProgress')));
const AppMenu = Loadable(lazy(() => import('./menu/AppMenu')));
const AppCheckbox = Loadable(lazy(() => import('./checkbox/AppCheckbox')));
const AppSwitch = Loadable(lazy(() => import('./switch/AppSwitch')));
const AppRadio = Loadable(lazy(() => import('./radio/AppRadio')));
const AppSlider = Loadable(lazy(() => import('./slider/AppSlider')));
const AppDialog = Loadable(lazy(() => import('./dialog/AppDialog')));
const AppSnackbar = Loadable(lazy(() => import('./snackbar/AppSnackbar')));
const AppAutoComplete = Loadable(lazy(() => import('./auto-complete/AppAutoComplete')));
const AppExpansionPanel = Loadable(lazy(() => import('./expansion-panel/AppExpansionPanel')));

const materialRoutes = [
  {
    path: '/clinic/material/table',
    element: <AppTable />,
  },
  {
    path: '/clinic/material/form',
    element: <AppForm />,
  },
  {
    path: '/clinic/material/buttons',
    element: <AppButton />,
  },
  {
    path: '/clinic/material/icons',
    element: <AppIcon />,
  },
  {
    path: '/clinic/material/progress',
    element: <AppProgress />,
  },
  {
    path: '/clinic/material/menu',
    element: <AppMenu />,
  },
  {
    path: '/clinic/material/checkbox',
    element: <AppCheckbox />,
  },
  {
    path: '/clinic/material/switch',
    element: <AppSwitch />,
  },
  {
    path: '/clinic/material/radio',
    element: <AppRadio />,
  },
  {
    path: '/clinic/material/slider',
    element: <AppSlider />,
  },
  {
    path: '/clinic/material/autocomplete',
    element: <AppAutoComplete />,
  },
  {
    path: '/material/expansion-panel',
    element: <AppExpansionPanel />,
  },
  {
    path: '/clinic/material/dialog',
    element: <AppDialog />,
  },
  {
    path: '/clinic/material/snackbar',
    element: <AppSnackbar />,
  },
];

export default materialRoutes;
