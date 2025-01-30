import UpdateUser from '../features/UpdateUser';
import { NavBar } from '../components/NavBar';
import { Footer } from '../components/Footer';

export default function UpdateUserPage(): JSX.Element {
  return (
    <div>
      <NavBar />
      <UpdateUser />
      <Footer />
    </div>
  );
}
