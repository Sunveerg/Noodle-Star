import { Home } from '../components/Home';
import { NavBar } from '../components/NavBar';

export default function HomePage(): JSX.Element {
  return (
    <div>
      <NavBar />
      <Home />
    </div>
  );
}
