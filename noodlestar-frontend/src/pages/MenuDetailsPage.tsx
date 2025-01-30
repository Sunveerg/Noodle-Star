import MenuDetails from '../features/MenuDetails';
import { NavBar } from '../components/NavBar';
import { Footer } from '../components/Footer';

export default function MenuDetailsPage(): JSX.Element {
  return (
    <div>
      <NavBar />
      <MenuDetails />
      <Footer />
    </div>
  );
}
