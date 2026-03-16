import API from './axios';

export const getUserTasksSummary = async (userId) => {
    try{
        const response = await API.get(`/users/${userId}/tasks/summary`);
        return response.data;
    }
    catch(error){
        console.error('Error fetching user task summary:', error);
        throw error;
    }
}

export const getUserTasks = async (userId) => {
    try{
        const response = await API.get(`/users/${userId}/tasks`);
        return response.data;
    }
    catch(error){
        console.error('Error fetching user tasks:', error);
        throw error;
    }
}

export const updateTaskStatus = async (userId,taskStatus) => {
    try {
        console.log("Updating task status for userId: ", userId, " with taskStatus: ", taskStatus);
        const response = await API.put(`/users/${userId}/tasks/status`, taskStatus);
        return response.data;
    } catch (error) {
        console.error('Error updating task status:', error);
        throw error;
    }
}

export const getUserTeams = async (userId) => {
    try {
        const response = await API.get(`/users/${userId}/teams`);
        return response.data;
    } catch (error) {
        console.error('Error fetching user teams:', error);
        throw error;
    }
}

