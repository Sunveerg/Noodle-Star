import React from "react";
import "../components/css/EmailSent.css";
import noodleImg from "../components/assets/noodle.png";

const EmailSent: React.FC = (): JSX.Element => {
    return (
        <div className="email-sent-container">
            <div className="email-sent-content">
                <h1 className="email-sent-title">
                    Your order is being prepared!
                </h1>
                <p className="email-sent-message">
                    An email has been sent to your inbox with the details of your order.
                </p>
                <img
                    src={noodleImg}
                    alt="Noodle"
                    className="noodle-img"
                />
            </div>
        </div>
    );
};

export default EmailSent;
