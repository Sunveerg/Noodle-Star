import { Home } from '../components/Home';
import { NavBar } from '../components/NavBar';
import { Footer } from '../components/Footer';

export default function HomePage(): JSX.Element {
  return (
    <div>
      <NavBar />
      <Home />
      <Footer />
    </div>
  );
}
