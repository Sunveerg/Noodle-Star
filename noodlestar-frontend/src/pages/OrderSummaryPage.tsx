import { NavBar } from '../components/NavBar';
import OrderSummary from '../features/OrderSummary';
import { Footer } from '../components/Footer';

export default function OrderSummaryPage(): JSX.Element {
  return (
    <div>
      <NavBar />
      <OrderSummary />
      <Footer />
    </div>
  );
}
