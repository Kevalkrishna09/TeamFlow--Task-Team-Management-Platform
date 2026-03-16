import React, { useEffect } from 'react'
import { useLocation } from 'react-router-dom'
import { sendInvitation } from '../api/invitation';
import { LoginForm } from '../components';
export const InvitationPage = () => {
    const location = useLocation();
    console.log("InvitationPage location ", location);
    const queryParams = new URLSearchParams(location.search);
    const token = queryParams.get('token');

    console.log("InvitationPage token ", token);

    const [loginPage, setLoginPage] = React.useState(false);
    const [invitationResponse, setInvitationResponse] = React.useState(null);
    useEffect(() => {   
        if(token){
            const fetchInvitationResponse = async () => { 
            try{
                 const response = await sendInvitation(token);
            console.log("Invitation response ", response);
            const responseData = await response.data;
            console.log("Invitation responseData ", responseData);
                setInvitationResponse(responseData);
            if(responseData.isExistingUser){
                window.alert("Welcome back! You have been added to the team. Please login to access your dashboard.");
                setLoginPage(true);

            }
            else if(responseData.isExistingUser === false){
                window.alert("You have been added to the team. Please sign up to access your dashboard.");
                setLoginPage(false);
            }
            else {
                window.alert("Invalid invitation token. Please check your invitation link or contact the team administrator.");
            }
            }
            catch(error){
                console.error("Error fetching invitation response ", error);
                window.alert("Your token is invalid or expired. Please check your invitation link or contact the team administrator.");
                setLoginPage(true);
            }
        }
        fetchInvitationResponse();
    } }, [token]);
console.log("About to render LoginForm with invitationResponse:", invitationResponse);
return (
  <div>
    {invitationResponse && (
      <LoginForm
        stateFromInvitation={loginPage ? "login" : "signup"}
        invitationToken={token}
        invitationResponse={invitationResponse}
      />
    )}
  </div>
)
};
