// Use lowercase in the import
import AvailableMenuList from '../features/AvailableMenuList';
import { NavBar } from '../components/NavBar';
import { Footer } from '../components/Footer';

export default function MenuOrderPage(): JSX.Element {
  return (
    <div>
      <NavBar />
      <AvailableMenuList />
      <Footer />
    </div>
  );
}
