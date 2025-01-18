import { createBrowserRouter } from 'react-router-dom';
import { PathRoutes } from './path.routes';
import HomePage from './pages/HomePage';
import AboutUsPage from './pages/AboutUsPage';
import MenuPage from './pages/MenuPage';
import MenuDetails from './pages/MenuDetailsPage';
import MenuOrderPage from './pages/MenuOrderPage';
import UpdateMenu from './features/UpdateMenu';
import ReviewPage from './pages/ReviewPage';
import AddReview from './features/Review/AddReview';
import OrderSummary from './pages/OrderSummaryPage';
import EmailSentPage from './pages/EmailSentPage';
import CallbackPage from './pages/Callbackpage';
import ProfilePage from './pages/ProfilePage';
import ManageStaffPage from './pages/ManageStaffPage';
import AddStaffPage from './pages/AddStaffPage';

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
    path: PathRoutes.Profile,
    element: <ProfilePage />,
  },
  {
    path: PathRoutes.Callback,
    element: <CallbackPage />,
  },
  {
    path: `${PathRoutes.MenuDetails}/:menuId`,
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
  {
    path: PathRoutes.OrderSummary,
    element: <OrderSummary />,
  },
  {
    path: PathRoutes.EmailSent,
    element: <EmailSentPage />,
  },
  {
    path: PathRoutes.ManageStaff,
    element: <ManageStaffPage />,
  },
  {
    path: PathRoutes.ManageUsers,
    element: <AddStaffPage />,
  },
]);

export default router;
