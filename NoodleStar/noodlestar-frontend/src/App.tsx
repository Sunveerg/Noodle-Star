import './App.css';
import { RouterProvider } from 'react-router-dom';
import router from './router';

function App(): JSX.Element {
    console.log('REACT_APP_BACKEND_URL:', process.env.REACT_APP_BACKEND_URL);
    return <RouterProvider router={router} />;
}

export default App;
