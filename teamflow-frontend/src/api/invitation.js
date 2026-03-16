import API from "./axios";

export const sendInvitation = async (tokenId) => {
    try{
        const response = await API.get(`/invitation/${tokenId}`);
        return response.data;
    } catch (error) {
        console.error("Error sending invitation", error);
        throw error;
    }
};