import MenuDetails from "../features/MenuDetails.tsx";
import {NavBar} from "../components/NavBar";


export default function MenuDetailsPage(): JSX.Element {
    return (
        <div>
            <NavBar />
            <MenuDetails />
        </div>
    );
}
