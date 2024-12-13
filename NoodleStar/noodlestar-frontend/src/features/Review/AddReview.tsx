import * as React from "react";
import { FormEvent, JSX, useState } from "react";
import { useNavigate } from "react-router-dom";
import { addReview } from "../api/Review/addReview.ts";
import { reviewRequestModel } from "../model/reviewRequestModel.ts";
import "../../components/css/AddReview.css";

interface ApiError {
    message: string;
}

const AddReview: React.FC = (): JSX.Element => {
    const [review, setReview] = useState<reviewRequestModel>({
        rating: 0,
        reviewerName: "",
        review: "",
        dateSubmitted: new Date(),
    });

    const [error, setError] = useState<{ [key: string]: string }>({});
    const [successMessage, setSuccessMessage] = useState<string>("");
    const [errorMessage, setErrorMessage] = useState<string>("");
    const [loading, setLoading] = useState<boolean>(false);
    const [showNotification, setShowNotification] = useState<boolean>(false);

    const navigate = useNavigate();

    const validate = (): boolean => {
        const newError: { [key: string]: string } = {};
        if (!review.rating || review.rating < 1 || review.rating > 5) {
            newError.rating = "Rating must be between 1 and 5";
        }
        if (!review.reviewerName) {
            newError.reviewerName = "Reviewer name is required";
        }
        if (!review.review) {
            newError.review = "Review text is required";
        }
        setError(newError);
        return Object.keys(newError).length === 0;
    };

    const handleSubmit = async (event: FormEvent<HTMLFormElement>): Promise<void> => {
        event.preventDefault();
        if (!validate()) {
            return;
        }
        setLoading(true);
        setErrorMessage("");
        setSuccessMessage("");
        setShowNotification(false);

        try {
            await addReview(review);
            setSuccessMessage("Review added successfully");
            setShowNotification(true);
            setTimeout(() => {
                navigate("/review");
            }, 2000);
        } catch (error) {
            const apiError = error as ApiError;
            setErrorMessage(`Error adding review: ${apiError.message}`);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="add-review-form">
            <h3 className="text-center">
                Add Review
            </h3>
            {loading && <div className="loader">Loading...</div>}
            <br />
            <div className="container">
                <form onSubmit={handleSubmit} className="text-center">
                    <div className="row">
                        <div className="col-4">
                            <div className="form-group">
                                <label>Rating (1-5)</label>
                                <input
                                    type={"number"}
                                    name={"rating"}
                                    placeholder={"Enter rating"}
                                    className={"form-control"}
                                    value={review.rating}
                                    onChange={(e) =>
                                        setReview((prev) => ({
                                            ...prev,
                                            rating: parseInt(e.target.value, 10),
                                        }))
                                    }
                                    required
                                />
                                {error.rating && <div className="text-danger">{error.rating}</div>}
                            </div>
                        </div>
                        <div className="col-4">
                            <div className="form-group">
                                <label>Reviewer Name</label>
                                <input
                                    type={"text"}
                                    name={"reviewerName"}
                                    placeholder={"Enter your name"}
                                    className={"form-control"}
                                    value={review.reviewerName}
                                    onChange={(e) =>
                                        setReview((prev) => ({
                                            ...prev,
                                            reviewerName: e.target.value,
                                        }))
                                    }
                                    required
                                />
                                {error.reviewerName && <div className="text-danger">{error.reviewerName}</div>}
                            </div>
                        </div>
                        <div className="col-4">
                            <div className="form-group">
                                <label>Review</label>
                                <textarea
                                    name={"review"}
                                    placeholder={"Enter your review"}
                                    className={"form-control"}
                                    value={review.review}
                                    onChange={(e) =>
                                        setReview((prev) => ({
                                            ...prev,
                                            review: e.target.value,
                                        }))
                                    }
                                    required
                                />
                                {error.review && <div className="text-danger">{error.review}</div>}
                            </div>
                        </div>
                    </div>
                    <br />
                    <div className={"row"}>
                        <button type={"submit"} className={"btn btn-primary"}>
                            Submit Review
                        </button>
                    </div>
                </form>
            </div>
            {errorMessage && <p className="error-message">{errorMessage}</p>}
            {showNotification && (
                <div className="notification">
                    <p>{successMessage}</p>
                </div>
            )}
        </div>
    );
};

export default AddReview;
