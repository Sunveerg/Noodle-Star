import * as React from 'react';
import { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './ReviewList.css';
import { reviewResponseModel } from '../model/reviewResponseModel';
import { getAllReview } from '../api/Review/getAllReview';
import noodleImg from '../../components/assets/noodle.png';

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
                </p>
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
