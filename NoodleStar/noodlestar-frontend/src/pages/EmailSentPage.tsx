import { NavBar } from '../components/NavBar';
import EmailSent from '../features/EmailSent.tsx';

export default function EmailSentPage(): JSX.Element {
  return (
    <div>
      <NavBar />
      <EmailSent />
    </div>
  );
}
