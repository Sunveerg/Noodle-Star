import { NavBar } from '../components/NavBar';
import Profile from '../features/Profile';
import { Footer } from '../components/Footer';
import AllReviewByUserId from '../features/Review/AllReviewByUserId';

export default function ProfilePage(): JSX.Element {
  return (
    <div>
      <NavBar />
      <Profile />
      <AllReviewByUserId />
      <Footer />
    </div>
  );
}
