import { AboutUs } from '../components/AboutUs';
import { NavBar } from '../components/NavBar';
import { Footer } from '../components/Footer';

export default function AboutUsPage(): JSX.Element {
  return (
    <div>
      <NavBar />
      <AboutUs />
      <Footer />
    </div>
  );
}
