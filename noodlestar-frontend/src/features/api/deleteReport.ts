import axios from 'axios';

const API_URL = 'api/v1/review';

export const deleteReport = async (reportId: string): Promise<void> => {
  try {
    await axios.delete(`${API_URL}/reports/${reportId}`);
  } catch (error) {
    console.error('Error deleting report:', error);
    throw error;
  }
};
