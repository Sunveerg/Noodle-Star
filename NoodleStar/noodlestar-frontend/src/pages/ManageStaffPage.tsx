import React from 'react';
import {NavBar} from "../components/NavBar";
import ManageStaff from "../features/ManageStaff.tsx";

export default function ManageStaffPage(): JSX.Element {
    return (
        <div>
            <NavBar/>
            <ManageStaff />
        </div>
    );
};