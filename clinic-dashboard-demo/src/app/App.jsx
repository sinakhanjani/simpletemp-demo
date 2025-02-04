// import '../fake-db';
import { Provider } from 'react-redux';
import { useRoutes } from 'react-router-dom';
import { MatxTheme } from './components';
import { AuthProvider } from './contexts/JWTAuthContext';
import { AppProvider } from './contexts/AppContext';
import { SettingsProvider } from './contexts/SettingsContext';
import { Store } from './redux/Store';
import routes from './routes';
import 'antd/dist/antd.css'; // or 'antd/dist/antd.less'

const App = () => {
  const content = useRoutes(routes);

  return (
    <Provider store={Store}>
      <SettingsProvider>
        <MatxTheme>
          <AppProvider>
            <AuthProvider>{content}</AuthProvider>
          </AppProvider>
        </MatxTheme>
      </SettingsProvider>
    </Provider>
  );
};

export default App;
