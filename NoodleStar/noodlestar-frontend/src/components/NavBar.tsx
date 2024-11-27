import React from 'react';
import { Navbar, Nav } from 'react-bootstrap';
import Container from 'react-bootstrap/Container';
import { Link } from 'react-router-dom';

const NavBar: React.FC = () => {
    const links = [
        { to: '/home', title: 'Home' },
        { to: '/aboutus', title: 'About Us' },
    ];

    return (
        <Navbar expand="lg" style={{ backgroundColor: 'lightblue' }}>
            <Container>
                <Navbar.Brand href="/">My App</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">
                        {links.map((link) => (
                            <Nav.Link key={link.to} as={Link} to={link.to}>
                                {link.title}
                            </Nav.Link>
                        ))}
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
};

export default NavBar;
