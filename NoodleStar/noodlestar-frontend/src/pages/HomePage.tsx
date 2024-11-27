import React from 'react';
import NavBar from "../components/NavBar";

const HomePage: React.FC = () => {
    return (
        <><NavBar/>
            <div style={{padding: '20px'}}>
                <h1>Welcome to the Home Page</h1>
                <p>This is the homepage of your app!</p>
            </div>
        </>
    );
};

export default HomePage;
