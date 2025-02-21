import { NavBar } from '../components/NavBar';
import DailyOrderReport from '../features/DailyOrderReport';

export default function DailyOrderPage(): JSX.Element {
  return (
    <div>
      <NavBar />
      <DailyOrderReport />
    </div>
  );
}
