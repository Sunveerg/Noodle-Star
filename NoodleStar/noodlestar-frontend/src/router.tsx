import { createBrowserRouter, Navigate } from 'react-router-dom';
import { AppRoutePaths } from './AppRoutePaths';


const router = createBrowserRouter([
    {
        path: AppRoutePaths.HomePage,
        element: <Navigate to={AppRoutePaths.HomePage} replace />,
        },
    {
        path: AppRoutePaths.AboutUsPage,
        element: <Navigate to={AppRoutePaths.AboutUsPage} replace />,
    },

]);

export default router;
