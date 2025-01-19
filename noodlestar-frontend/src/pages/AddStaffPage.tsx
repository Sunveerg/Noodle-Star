import { NavBar } from '../components/NavBar';
import ManageUsers from '../features/ManageUsers';

export default function AddStaffPage(): JSX.Element {
  return (
    <div>
      <NavBar />
      <ManageUsers />
    </div>
  );
}
