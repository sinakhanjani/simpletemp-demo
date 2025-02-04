import CalendarMonthIcon from '@mui/icons-material/CalendarMonth';
import PostAddIcon from '@mui/icons-material/PostAdd';
import AccountBalanceWalletIcon from '@mui/icons-material/AccountBalanceWallet';
import QuestionAnswerIcon from '@mui/icons-material/QuestionAnswer';
import HelpRoundedIcon from '@mui/icons-material/HelpRounded';
import SupportAgentIcon from '@mui/icons-material/SupportAgent';

export const navigations = [
  { name: 'Home', path: '/clinic/dashboard/', icon: 'home' },
  { name: 'Calendar', path: '/clinic/calendar' , icon: <CalendarMonthIcon fontSize="small"/> },
  { name: 'Post a Shift', path: '/clinic/calendar/post-shift', icon: <PostAddIcon fontSize="small"/> },
  { name: 'Money', path: '/clinic/invoice', icon: <AccountBalanceWalletIcon fontSize="small"/> },
  { name: 'Support/Chat', path: '/clinic/ticket-list', icon: <QuestionAnswerIcon fontSize="small"/> },
  { name: 'FAQ', path: '/clinic/faq', icon: <HelpRoundedIcon fontSize="small"/> },

  // { label: 'Components', type: 'label' },
  // {
  //   name: 'Components',
  //   icon: 'favorite',
  //   badge: { value: '30+', color: 'secondary' },
  //   children: [
  //     { name: 'Auto Complete', path: '/clinic/material/autocomplete', iconText: 'A' },
  //     { name: 'Buttons', path: '/clinic/material/buttons', iconText: 'B' },
  //     { name: 'Checkbox', path: '/clinic/material/checkbox', iconText: 'C' },
  //     { name: 'Dialog', path: '/clinic/material/dialog', iconText: 'D' },
  //     { name: 'Expansion Panel', path: '/clinic/material/expansion-panel', iconText: 'E' },
  //     { name: 'Form', path: '/clinic/material/form', iconText: 'F' },
  //     { name: 'Icons', path: '/clinic/material/icons', iconText: 'I' },
  //     { name: 'Menu', path: '/clinic/material/menu', iconText: 'M' },
  //     { name: 'Progress', path: '/clinic/material/progress', iconText: 'P' },
  //     { name: 'Radio', path: '/clinic/material/radio', iconText: 'R' },
  //     { name: 'Switch', path: '/clinic/material/switch', iconText: 'S' },
  //     { name: 'Slider', path: '/clinic/material/slider', iconText: 'S' },
  //     { name: 'Snackbar', path: '/clinic/material/snackbar', iconText: 'S' },
  //     { name: 'Table', path: '/clinic/material/table', iconText: 'T' },
  //   ],
  // },
  // {
  //   name: 'Charts',
  //   icon: 'trending_up',
  //   children: [{ name: 'Echarts', path: '/clinic/charts/echarts', iconText: 'E' }],
  // },
  // {
  //   name: 'Documentation',
  //   icon: 'launch',
  //   type: 'extLink',
  //   path: 'http://demos.ui-lib.com/matx-react-doc/',
  // },
];
