import './App.css';
import { RouterProvider } from 'react-router-dom';
import router from './router';
import 'bootstrap/dist/css/bootstrap.min.css';

function App(): JSX.Element {
  // eslint-disable-next-line no-console
  console.log('REACT_APP_BACKEND_URL:', process.env.REACT_APP_BACKEND_URL);
  return <RouterProvider router={router} />;
}

export default App;
