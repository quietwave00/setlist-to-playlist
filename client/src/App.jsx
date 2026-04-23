import { AppProvider } from './store/useAppStore.jsx';
import Home from './pages/Home.jsx';
import Toast from './components/Toast.jsx';
import './App.css';

const App = () => {
    return (
        <AppProvider>
            <Home />
            <Toast />
        </AppProvider>
    );
};

export default App;
