import API from './axios';

export const task = async (teamId,formdata) => {
  try {
    console.log("formdata in api " + formdata, teamId);
    const response = await API.post(`/teams/${teamId}/task`, formdata);
    return response.data;
  } catch (error) {
    console.error('Error creating task:', error);
    throw error;
  }
};
 
export const deleteTask = async (taskId) => {
  try {
    const response = await API.delete(`/task/${taskId}`);
    return response.data;
  } catch (error) {
    console.error('Error deleting task:', error);
    throw error;
  }
}