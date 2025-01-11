import Login from "../features/Login";
import {NavBar} from "../components/NavBar";

export default function LoginPage(): JSX.Element {
    return (
        <div>
            <NavBar/>
            <Login />
        </div>
    );
};

