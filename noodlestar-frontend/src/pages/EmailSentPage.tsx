import { NavBar } from '../components/NavBar';
import EmailSent from '../features/EmailSent';
import { Footer } from '../components/Footer';

export default function EmailSentPage(): JSX.Element {
  return (
    <div>
      <NavBar />
      <EmailSent />
      <Footer />
    </div>
  );
}
