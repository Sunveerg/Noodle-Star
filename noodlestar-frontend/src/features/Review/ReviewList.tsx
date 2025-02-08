import * as React from 'react';
import { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './ReviewList.css';
import { reviewResponseModel } from '../model/reviewResponseModel';
import { getAllReview } from '../api/Review/getAllReview';
import noodleImg from '../../components/assets/noodle.png';

const API_URL = 'http://localhost:8080/api/v1/review'; // Adjust port if needed

const ReviewList: React.FC = (): JSX.Element => {
  const [reviews, setReviews] = useState<reviewResponseModel[]>([]);

  useEffect(() => {
    const fetchReviewData = async (): Promise<void> => {
      try {
        const response = await getAllReview();
        if (Array.isArray(response)) {
          setReviews(response);
        } else {
          console.error('Fetched data is not an array:', response);
        }
      } catch (error) {
        console.error('Error fetching reviews:', error);
      }
    };

    fetchReviewData().catch(error =>
      console.error('Error in fetchReviewData:', error)
    );
  }, []);

  // eslint-disable-next-line @typescript-eslint/explicit-function-return-type
  const handleDelete = async (reviewId: string) => {
    try {
      const response = await fetch(`${API_URL}/${reviewId}`, {
        method: 'DELETE',
        credentials: 'include',
      });

      if (!response.ok) {
        throw new Error(`Failed to delete review (status: ${response.status})`);
      }

      // Remove the deleted review from state
      setReviews(prevReviews =>
        prevReviews.filter(review => review.reviewId !== reviewId)
      );
    } catch (err) {
      console.error('Failed to delete review:', err);
      alert('Error deleting review');
    }
  };

  // eslint-disable-next-line @typescript-eslint/explicit-function-return-type
  const generateStars = (rating: number) => {
    const stars = [];
    for (let i = 0; i < 5; i++) {
      stars.push(
        <span key={i} className={i < rating ? 'star filled' : 'star empty'}>
          ★
        </span>
      );
    }
    return stars;
  };

  return (
    <div className="titleSection">
      <h2 className="pageTitle">
        Customer Reviews
        <img
          src={noodleImg}
          alt="Noodle"
          style={{ width: '100px', height: '100px' }}
        />
      </h2>
      <div className="topRightImage"></div>
      <div className="review-list">
        {reviews.length > 0 ? (
          reviews.map(review => (
            <div className="review-item" key={review.reviewId}>
              <div className="review-item-content">
                <div className="review-rating">
                  {generateStars(review.rating)}
                </div>
                <div className="reviewer-name">{review.reviewerName}</div>
                <p className="review-text">{review.review}</p>
                <p className="review-date">
                  {new Date(review.dateSubmitted).toLocaleDateString()}
                </p>
                <button
                  className="delete-btn"
                  onClick={() => handleDelete(review.reviewId)}
                >
                  ❌
                </button>
              </div>
            </div>
          ))
        ) : (
          <p className="no-items">No reviews available</p>
        )}
      </div>
    </div>
  );
};

export default ReviewList;
