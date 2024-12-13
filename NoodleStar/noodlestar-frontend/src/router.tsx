import { createBrowserRouter } from 'react-router-dom';
import { PathRoutes } from './path.routes';
import HomePage from './pages/HomePage';
import AboutUsPage from './pages/AboutUsPage';
import MenuPage from './pages/MenuPage';
import MenuDetails from './pages/MenuDetailsPage';
import MenuOrderPage from './pages/MenuOrderPage';
import UpdateMenu from "./features/UpdateMenu.tsx";
import ReviewPage from './pages/ReviewPage.tsx';
import AddReview from "./features/Review/AddReview.tsx";





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
    {
        path: `${PathRoutes.Menu}/:menuId`,
        element: <MenuDetails />,
    },
    {
        path: PathRoutes.MenuOrder,
        element: <MenuOrderPage />,
    },
    {
        path: PathRoutes.UpdateMenu,
        element: <UpdateMenu />,
    },
    {
        path: PathRoutes.Review,
        element: <ReviewPage />,
    },
    {
        path: PathRoutes.AddReview,
        element: <AddReview />,
    },
]);

export default router;
