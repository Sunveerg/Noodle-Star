import React, { useEffect, useState } from 'react';
import { reviewResponseModel } from '../model/reviewResponseModel';
import { getReviewByUserId } from '../api/Review/getReviewByUserId';
import './ReviewList.css';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

interface ApiError {
  message: string;
}

const AllReviewByUserId: React.FC = (): JSX.Element => {
  const [reviews, setReviews] = useState<reviewResponseModel[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [userId, setUserId] = useState<string | null>(null);
  const navigate = useNavigate();

  // eslint-disable-next-line @typescript-eslint/explicit-function-return-type
  const handleDelete = async (reviewId: string) => {
    const backendUrl = process.env.REACT_APP_BACKEND_URL;
    try {
      const url = `${backendUrl}/api/v1/review/${reviewId}`;
      await axios.delete(url);
      setReviews(prevReviews =>
        prevReviews.filter(review => review.reviewId !== reviewId)
      );
    } catch (error: unknown) {
      if (error instanceof Error) {
        throw new Error(
          `Failed to delete review with id ${reviewId}: ${error.message}`
        );
      } else {
        throw new Error(
          `Failed to delete review with id ${reviewId}: Unknown error`
        );
      }
    }
  };

  const decodeAccessToken = (accessToken: string): string | null => {
    try {
      const base64Url = accessToken.split('.')[1];
      const decodedPayload = JSON.parse(atob(base64Url));
      return decodedPayload.sub || null; // Ensure sub (userId) is a string or null
    } catch (error) {
      console.error('Error decoding access token:', error);
      return null;
    }
  };

  useEffect((): void => {
    const accessToken: string | null = localStorage.getItem('access_token');
    if (accessToken) {
      const decodedUserId = decodeAccessToken(accessToken);
      if (decodedUserId) {
        setUserId(decodedUserId); // Set userId as a string
      } else {
        setError('Failed to decode user ID from the access token');
        setLoading(false);
      }
    } else {
      setError('🚫👤');
      setLoading(false);
    }
  }, []);

  useEffect((): void => {
    if (userId) {
      const fetchReviews = async (id: string): Promise<void> => {
        try {
          const data: reviewResponseModel[] = await getReviewByUserId(id); // Pass userId as a string
          setReviews(data);
        } catch (error) {
          const apiError = error as ApiError;
          setError(apiError.message || 'Error fetching reviews');
        } finally {
          setLoading(false);
        }
      };
      fetchReviews(userId);
    }
  }, [userId]);

  if (loading) {
    return <p>Loading reviews...</p>;
  }

  if (error) {
    return <p className={'error-message'}>{error}</p>;
  }
  // eslint-disable-next-line @typescript-eslint/explicit-function-return-type
  const generateStars = (rating: number) => {
    const stars = [];
    for (let i = 0; i < 5; i++) {
      if (i < rating) {
        stars.push(
          <span key={i} className="star filled">
            ★
          </span>
        ); // Filled star (gold)
      } else {
        stars.push(
          <span key={i} className="star empty">
            ★
          </span>
        ); // Empty star (gray)
      }
    }
    return stars;
  };

  return (
    <div className="titleSection">
      <h2 className="pageTitle">Your Reviews</h2>
      <div className="topRightImage"></div>
      <div className="review-list">
        {reviews.length > 0 ? (
          reviews.map(review => (
            <div className="review-item" key={review.reviewId}>
              <div className="review-item-content">
                {/* Rating at the top */}
                <div className="review-rating">
                  {generateStars(review.rating)}
                </div>

                {/* Name below rating */}
                <div className="reviewer-name">{review.reviewerName}</div>

                {/* Review text */}
                <p className="review-text">{review.review}</p>

                {/* Review date */}
                <p className="review-date">
                  {new Date(review.dateSubmitted).toLocaleDateString()}
                  {review.isEdited && (
                    <span className="edited-label"> • (Edited)</span>
                  )}
                </p>
                <button
                  className="delete-btn"
                  onClick={() => handleDelete(review.reviewId)}
                >
                  ❌
                </button>
                <button
                  onClick={e => {
                    e.stopPropagation();
                    navigate(`review/${review.reviewId}/update`);
                  }}
                  className="btn-edit"
                >
                  ✏️️
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

export default AllReviewByUserId;
