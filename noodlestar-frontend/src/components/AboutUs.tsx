import React, { useState, useEffect } from 'react';
import styles from './css/AboutUs.module.css';
import { TextBlock } from './TextBlock';
import noodleImg from './assets/noodle.png';
import { teamMemberResponseModel } from '../features/model/teamMemberResponseModel';

const textBlocks = [
  {
    title: 'History',
    content:
      'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.',
  },
  {
    title: 'Mission & Vision Statements',
    content:
      'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.',
  },
];

export const AboutUs: React.FC = () => {
  const [teamMembers, setTeamMembers] = useState<teamMemberResponseModel[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const backendUrl = process.env.REACT_APP_BACKEND_URL;

  const fetchTeamMembers = async () => {
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
  };

  useEffect(() => {
    fetchTeamMembers();
  }, []);

  if (loading) {
    return <div className={styles.loadingMessage}>Loading team members...</div>;
  }

  if (error) {
    return <div className={styles.errorMessage}>{error}</div>;
  }

  return (
    <main className={styles.aboutUs}>
      <div className={styles.cloud3}></div>
      <div className={styles.cloud4}></div>
      <div className={styles.cloud5}></div>
      <div className={styles.cloud6}></div>

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
