import './App.css';
import { RouterProvider } from 'react-router-dom';
import router from './router';
import 'bootstrap/dist/css/bootstrap.min.css';

function App(): JSX.Element {
  return <RouterProvider router={router} />;
}

export default App;
