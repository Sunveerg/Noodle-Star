import axiosInstance from '../../Shared/Api/axiosInstance';

export const sendEmail = async (
  to: string,
  subject: string,
  body: string
): Promise<string> => {
  try {
    const response = await axiosInstance.get<string>('/send-email', {
      params: { to, subject, body },
    });
    return response.data;
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
  } catch (error: any) {
    console.error('Failed to send email:', error);
    if (error.response) {
      throw new Error(error.response.data.message || 'Failed to send email');
    }
    if (error.request) {
      throw new Error('No response from the server');
    }
    throw new Error('Network error occurred');
  }
};
