import API from './axios';

export const loginUser = (data, token)=>{
    console.log("inside loginUser--data is", data, "token is ", token);
    if(token)
        return API.post(`/auth/login?invitationToken=${token}`,data);
   return  API.post(`/auth/login`,data);
}

export const signupUser = (data, token) => {
    console.log("inside signupUser--data is", data, "token is ", token);
    if(token)
        return API.post(`/auth/register?invitationToken=${token}`, data);
    return API.post(`/auth/register/`, data);
}