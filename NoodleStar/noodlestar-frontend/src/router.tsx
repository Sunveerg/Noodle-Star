import { createBrowserRouter } from 'react-router-dom';
import { PathRoutes } from './path.routes';
import HomePage from './pages/HomePage';
import AboutUsPage from './pages/AboutUsPage';
import MenuPage from './pages/MenuPage';




const router = createBrowserRouter([
    {
        path: PathRoutes.HomePage,
        element: <HomePage />,
        },
    {
        path: PathRoutes.AboutUsPage,
        element: <AboutUsPage />,
    },

    {
        path: PathRoutes.Menu,
        element: <MenuPage />,
    },

]);

export default router;
