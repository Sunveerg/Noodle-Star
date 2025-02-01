import FinancialReport from '../features/FinancialReport';
import { NavBar } from '../components/NavBar';

export default function FinancialReportPage(): JSX.Element {
  return (
    <div>
      <NavBar />
      <FinancialReport />
    </div>
  );
}
