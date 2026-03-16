import API from './axios';

export const getTeam = async (teamId) => {
  try {
    const response = await API.get(`/teams/${teamId}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching team:', error);
    throw error;
  }
};

export const getAllTeams = async () => {
  try {
    console.log('Fetching all teams from API...');
    const response = await API.get(`/teams`);
    return response.data;
  } catch (error) {
    console.error('Error fetching teams:', error);
    throw error;
  }
};

export const team = async (data)=>{
    console.log("inside createTeam--", data)
   return  API.post("/teams",data);
}

export const teamMembers = async (teamId) => {
  console.log("Fetching team members for teamId: ", teamId);
  return API.get(`/teams/${teamId}/members`);
}

export const teamTasks = async (teamId) => {
  console.log("Fetching team tasks for teamId: ", teamId);
  return API.get(`/teams/${teamId}/tasks`);
}