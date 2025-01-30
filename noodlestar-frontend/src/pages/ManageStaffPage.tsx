import { NavBar } from '../components/NavBar';
import ManageStaff from '../features/ManageStaff';
import { Footer } from '../components/Footer';

export default function ManageStaffPage(): JSX.Element {
  return (
    <div>
      <NavBar />
      <ManageStaff />
      <Footer />
    </div>
  );
}
