import { AppProvider } from './store/useAppStore.jsx';
import Home from './pages/Home.jsx';
import './App.css';

const App = () => {
    return (
        <AppProvider>
            <Home />
        </AppProvider>
    );
};

export default App;