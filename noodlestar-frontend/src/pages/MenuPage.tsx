import MenuList from '../features/MenuList2'; // Use lowercase in the import
import { NavBar } from '../components/NavBar';
import { Footer } from '../components/Footer';

export default function MenuPage(): JSX.Element {
  return (
    <div>
      <NavBar />
      <MenuList />
      <Footer />
    </div>
  );
}
