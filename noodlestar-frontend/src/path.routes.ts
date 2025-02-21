export enum PathRoutes {
  Default = '/',
  HomePage = '/home',
  AboutUsPage = '/aboutus',
  Menu = '/menu',
  MenuDetails = '/menu/:menuId',
  MenuOrder = '/menuOrder',
  UpdateMenu = '/menu/:menuId/update',
  Review = '/review',
  AddReview = 'review/add',
  OrderSummary = '/orderSummary',
  Orders = '/orders',
  EmailSent = 'orderSummary/emailSent',
  Login = '/login',
  Callback = '/callback',
  Profile = '/profile',
  ManageStaff = '/manageStaff',
  UpdateStaff = '/manageStaff/:staffId/update',
  ManageUsers = '/addStaff',
  UpdateUsers = '/updateUsers/:userId',
  Reports = '/reports',
  FinancialReport = '/financial-report',
  DailyReport = '/daily-order',
  UpdateReview = 'profile/review/:reviewId/update',
}
