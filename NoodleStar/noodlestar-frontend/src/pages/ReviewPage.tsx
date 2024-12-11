import {NavBar} from "../components/NavBar";
import ReviewList from "../features/Review/ReviewList";


export default function ReviewPage(): JSX.Element {
  return (
    <div>
      <NavBar />
      <ReviewList />
    </div>
  );
}
