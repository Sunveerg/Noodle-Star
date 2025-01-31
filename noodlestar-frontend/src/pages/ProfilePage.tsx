import { NavBar } from '../components/NavBar';
import Profile from '../features/Profile';
import { Footer } from '../components/Footer';

export default function ProfilePage(): JSX.Element {
  return (
    <div>
      <NavBar />
      <Profile />
      <Footer />
    </div>
  );
}
