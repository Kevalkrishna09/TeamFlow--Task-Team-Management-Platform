import React from 'react'
import { useEffect } from 'react';
import { Header,Footer } from './components/index';
import { AllRoutes } from './routes/AllRoutes';
import { useAuth } from './context/AuthContext';
import { useNavigate, useLocation } from 'react-router-dom';

export const App = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { user } = useAuth();

  useEffect(() => {
    const publicRoutes = ["/login", "/signup", "/invitation"];
    // Allow invitation with token param
    const isInvitationRoute = location.pathname.startsWith("/invitation");
    if (!user && !publicRoutes.includes(location.pathname) && !isInvitationRoute) {
      console.log("No user found, navigating to login page");
      navigate("/login");
    }
  }, [user, navigate, location.pathname]);

  return (
    <div className="App min-h-screen flex flex-col">
      {user && <Header />}
      <main className="main-content flex-1">
        <AllRoutes />
      </main>
      {user && <Footer />}
    </div>
  );
};