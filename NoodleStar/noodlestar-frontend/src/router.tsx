import { createBrowserRouter, Navigate } from 'react-router-dom';
import { AppRoutePaths } from './AppRoutePaths';


import HomePage from "./pages/HomePage";
import AboutUsPage from "./pages/AboutUsPage";

const router = createBrowserRouter([
    {
        path: AppRoutePaths.HomePage,
        element: <HomePage />,
    },
    {
        path: AppRoutePaths.AboutUsPage,
        element: <AboutUsPage />,
    },

]);

export default router;
