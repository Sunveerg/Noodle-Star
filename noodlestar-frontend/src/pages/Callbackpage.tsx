import { NavBar } from '../components/NavBar';
import Callback from '../features/Callback';
import { Footer } from '../components/Footer';

export default function CallbackPage(): JSX.Element {
  return (
    <div>
      <NavBar />
      <Callback />
      <Footer />
    </div>
  );
}
