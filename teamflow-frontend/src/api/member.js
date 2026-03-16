import API from './axios';

export const member = async (teamId, formdata) => {
  try {
    const response = await API.post(`/teams/${teamId}/invite`, formdata);
    return response.data;
  } catch (error) {
    console.error('Error inviting member:', error);
    throw error;
  }
};