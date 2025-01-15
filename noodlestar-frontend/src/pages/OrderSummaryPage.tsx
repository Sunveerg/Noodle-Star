import { NavBar } from '../components/NavBar';
import OrderSummary from '../features/OrderSummary';

export default function OrderSummaryPage(): JSX.Element {
  return (
    <div>
      <NavBar />
      <OrderSummary />
    </div>
  );
}
