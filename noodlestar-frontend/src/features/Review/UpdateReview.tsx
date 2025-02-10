import * as React from 'react';
import { FormEvent, JSX, useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { getReviewById, updateReview } from '../api/Review/updateReview';
import { reviewRequestModel } from '../model/reviewRequestModel';
import '../../components/css/UpdateReview.css';

interface ApiError {
  message: string;
}

const UpdateReview: React.FC = (): JSX.Element => {
  const { reviewId } = useParams<{ reviewId: string }>();
  const [review, setReview] = useState<reviewRequestModel>({
    reviewerName: '',
    userId: '',
    review: '',
    rating: 0,
    dateSubmitted: new Date(),
    isEdited: false,
  });
  const [error, setError] = useState<{ [key: string]: string }>({});
  const [successMessage, setSuccessMessage] = useState<string>('');
  const [errorMessage, setErrorMessage] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);
  const [showNotification, setShowNotification] = useState<boolean>(false);

  const navigate = useNavigate();

  useEffect(() => {
    const fetchReview = async (): Promise<void> => {
      if (reviewId) {
        try {
          const response = await getReviewById(reviewId);
          setReview({
            reviewerName: response.reviewerName,
            userId: response.userId,
            review: response.review,
            rating: response.rating,
            dateSubmitted: new Date(response.dateSubmitted),
            isEdited: true,
          });
        } catch (error) {
          console.error(`Error fetching review with id ${reviewId}`, error);
        }
      }
    };
    fetchReview().catch(error => console.error('Error fetching review', error));
  }, [reviewId]);

  const validate = (): boolean => {
    const newError: { [key: string]: string } = {};
    if (!review.reviewerName) {
      newError.reviewerName = 'Reviewer name is required';
    }
    if (!review.review) {
      newError.review = 'Review text is required';
    }
    if (review.rating < 1 || review.rating > 5) {
      newError.rating = 'Rating must be between 1 and 5';
    }
    setError(newError);
    return Object.keys(newError).length === 0;
  };

  const handleSubmit = async (
    event: FormEvent<HTMLFormElement>
  ): Promise<void> => {
    event.preventDefault();
    if (!validate()) {
      return;
    }
    setLoading(true);
    setErrorMessage('');
    setSuccessMessage('');
    setShowNotification(false);

    try {
      if (reviewId) {
        const updatedReview = { ...review, isEdited: true };
        await updateReview(reviewId, updatedReview);
        setSuccessMessage('Review updated successfully');
        setShowNotification(true);
        setTimeout(() => {
          navigate('/profile');
        }, 2000);
      }
    } catch (error) {
      const apiError = error as ApiError;
      setErrorMessage(`Error updating review: ${apiError.message}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="edit-review-form">
      <h3 className="text-center">
        Review &nbsp;
        <small className="text-muted">Edit Form</small>
      </h3>
      {loading && <div className="loader">Loading...</div>}
      <br />
      <div className="container">
        <form onSubmit={handleSubmit} className="text-center">
          <div className="form-group">
            <label>Reviewer Name</label>
            <input
              type="text"
              name="reviewerName"
              placeholder="Enter reviewer name"
              className="form-control"
              value={review.reviewerName}
              onChange={e =>
                setReview(prev => ({ ...prev, reviewerName: e.target.value }))
              }
              required
            />
            {error.reviewerName && (
              <div className="text-danger">{error.reviewerName}</div>
            )}
          </div>
          <div className="form-group">
            <label>Review</label>
            <textarea
              name="review"
              placeholder="Enter review"
              className="form-control"
              value={review.review}
              onChange={e =>
                setReview(prev => ({ ...prev, review: e.target.value }))
              }
              required
            />
            {error.review && <div className="text-danger">{error.review}</div>}
          </div>
          <div className="form-group">
            <label>Rating (1-5)</label>
            <input
              type="number"
              name="rating"
              className="form-control"
              value={review.rating}
              onChange={e =>
                setReview(prev => ({
                  ...prev,
                  rating: parseInt(e.target.value, 10),
                }))
              }
              required
              min="1"
              max="5"
            />
            {error.rating && <div className="text-danger">{error.rating}</div>}
          </div>
          <br />
          <button type="submit" className="btn btn-primary">
            Update
          </button>
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

export default UpdateReview;
