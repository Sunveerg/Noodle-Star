/* eslint-disable @typescript-eslint/explicit-function-return-type */
import React, { useState, useEffect, useCallback } from 'react';
import styles from './css/AboutUs.module.css';
import { TextBlock } from './TextBlock';
import noodleImg from './assets/noodle.png';
import { teamMemberResponseModel } from '../features/model/teamMemberResponseModel';

const textBlocks = [
  {
    title: 'History',
    content:
      'Founded in the heart of Montreal, Nouilles Star has been serving authentic Chinese, Cantonese, Szechuanese, and Vietnamese cuisine for years. Formerly known as Soupe & Nouilles, our restaurant has built a reputation for bold flavors, handcrafted noodle dishes, and rich broths that bring a taste of Asia to every bowl. Inspired by traditional family recipes and modern culinary techniques, we take pride in delivering an unforgettable dining experience to our guests.',
  },
  {
    title: 'Mission',
    content:
      'At Nouilles Star, our mission is to bring the warmth and authenticity of Asian cuisine to Montreal, offering handcrafted noodles, flavorful broths, and a diverse menu that reflects our passion for culinary excellence. We are committed to using fresh ingredients, traditional cooking methods, and friendly service to create an inviting space where every meal feels like home.',
  },
];

export const AboutUs: React.FC = () => {
  const [teamMembers, setTeamMembers] = useState<teamMemberResponseModel[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [isOwner, setIsOwner] = useState<boolean>(false);
  const backendUrl = process.env.REACT_APP_BACKEND_URL;

  // Fetch user role from the JWT token
  useEffect(() => {
    const accessToken = localStorage.getItem('access_token');
    if (!accessToken) {
      setIsOwner(false);
      return;
    }

    try {
      const base64Url = accessToken.split('.')[1];
      const decodedPayload = JSON.parse(atob(base64Url));
      const roles = decodedPayload['https://noodlestar/roles'] || [];
      setIsOwner(roles.includes('Owner'));
    } catch (err) {
      console.error('Error decoding user roles:', err);
      setIsOwner(false);
    }
  }, []);

  // Fetch team members from the backend
  const fetchTeamMembers = useCallback(async (): Promise<void> => {
    try {
      const response = await fetch(`${backendUrl}/api/v1/team-members`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) {
        setError('Failed to fetch team members');
        return;
      }

      const teamData: teamMemberResponseModel[] = await response.json();
      setTeamMembers(teamData);
    } catch (err) {
      setError('Error fetching team members');
      console.error('Error fetching team members:', err);
    } finally {
      setLoading(false);
    }
  }, [backendUrl]);

  // Delete a team member
  const handleDelete = async (teamMemberId: string) => {
    if (!window.confirm('Are you sure you want to delete this team member?')) {
      return;
    }

    try {
      const response = await fetch(
        `${backendUrl}/api/v1/team-members/${teamMemberId}`,
        {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json',
          },
        }
      );

      if (!response.ok) {
        alert('Failed to delete team member');
        return;
      }

      // Update the state to reflect the deletion
      setTeamMembers(prevMembers =>
        prevMembers.filter(member => member.teamMemberId !== teamMemberId)
      );
    } catch (err) {
      console.error('Error deleting team member:', err);
      alert('Error deleting team member');
    }
  };

  useEffect(() => {
    fetchTeamMembers();
  }, [fetchTeamMembers]);

  if (loading) {
    return <div className={styles.loadingMessage}>Loading team members...</div>;
  }

  if (error) {
    return <div className={styles.errorMessage}>{error}</div>;
  }

  return (
    <main className={styles.aboutUs}>
      <div className={styles.topRightImage}></div>

      <section className={styles.content}>
        <div className={styles.titlePart}>
          <h2 className={styles.mainTitle}>
            About Us{' '}
            <img
              src={noodleImg}
              alt="Noodle"
              style={{ width: '100px', height: '100px' }}
            />
          </h2>
        </div>

        <article className={styles.textBlocks}>
          {textBlocks.map(block => (
            <TextBlock
              key={block.title}
              title={block.title}
              content={block.content}
            />
          ))}
        </article>

        {/* Display team members */}
        <section className={styles.teamSection}>
          <h3>Our Team</h3>
          {teamMembers.length > 0 ? (
            <div className={styles.teamMembers}>
              {teamMembers.map(member => (
                <div key={member.teamMemberId} className={styles.teamMember}>
                  <div className={styles.teamMemberPhoto}>
                    <img
                      src={member.photoUrl}
                      alt={member.name}
                      style={{
                        width: '100px',
                        height: '100px',
                        borderRadius: '50%',
                      }}
                    />
                  </div>
                  <div className={styles.teamMemberInfo}>
                    <h4>{member.name}</h4>
                    <p>
                      <strong>Role:</strong> {member.role}
                    </p>
                    <p>{member.bio}</p>

                    {/* Show delete button only if user is an Owner */}
                    {isOwner && (
                      <button
                        className={styles.deleteButton}
                        onClick={() => handleDelete(member.teamMemberId)}
                      >
                        Delete
                      </button>
                    )}
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <p>No team members available.</p>
          )}
        </section>
      </section>
    </main>
  );
};
