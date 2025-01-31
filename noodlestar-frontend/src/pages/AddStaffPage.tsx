import { NavBar } from '../components/NavBar';
import ManageUsers from '../features/ManageUsers';
import { Footer } from '../components/Footer';

export default function AddStaffPage(): JSX.Element {
  return (
    <div>
      <NavBar />
      <ManageUsers />
      <Footer />
    </div>
  );
}
