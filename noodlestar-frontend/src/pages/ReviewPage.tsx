import { NavBar } from '../components/NavBar';
import ReviewList from '../features/Review/ReviewList';
import { Footer } from '../components/Footer';

export default function ReviewPage(): JSX.Element {
  return (
    <div>
      <NavBar />
      <ReviewList />
      <Footer />
    </div>
  );
}
