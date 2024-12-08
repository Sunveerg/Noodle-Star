import React, { useState } from "react";
import { AboutUs } from "../components/AboutUs";
import { NavBar } from "../components/NavBar";
import { Modal, Button } from "react-bootstrap";
import 'bootstrap/dist/css/bootstrap.min.css';

export default function AboutUsPage(): JSX.Element {
    const [showModal, setShowModal] = useState(false);

    const handleClose = () => setShowModal(false);
    const handleShow = () => setShowModal(true);

    return (
        <div>
            <NavBar />
            <AboutUs />
            <div style={{ textAlign: "center", marginTop: "20px" }}>
                <Button variant="primary" onClick={handleShow}>
                    Learn More About Us
                </Button>
            </div>
            <Modal show={showModal} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>About Us</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    Welcome to our company! We are dedicated to providing the best services to our clients and
                    building a community of trust and innovation.
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Close
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
}
