import ReportList from '../features/ReportList';
import { NavBar } from '../components/NavBar';

export default function ReportPage(): JSX.Element {
  return (
    <div>
      <NavBar />
      <ReportList />
    </div>
  );
}
