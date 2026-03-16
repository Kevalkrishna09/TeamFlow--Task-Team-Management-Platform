// filepath: /Users/kkris04/Documents/Personal/teamflowProject/teamflow-frontend/src/pages/Admin.jsx
import React, { useEffect, useState } from 'react';
import { getAllTeams, getTeam } from '../api/team';
import TeamCard from '../components/TeamCard';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export const Admin = () => {
  const navigate = useNavigate();
  const [teams, setTeams] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { user } = useAuth();
  const createTeam = () => {
    console.log("create team function called");
    navigate("/admin/team");
  }
  useEffect(() => {
    const fetchTeams = async () => {
      try {
        console.log("user object in admin page ", user);
        const data = await getAllTeams();
        console.log('Fetched teams:', data);
        setTeams(data);
      } catch (err) {
        console.error('Error fetching teams:', err);
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchTeams();
  }, []);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      {/* Intro Section */}
      <section className="bg-white rounded-lg shadow-md p-6 mb-8">
        <h1 className="text-3xl font-bold text-gray-800 mb-2">Admin Dashboard</h1>
        <p className="text-gray-600 text-lg">Welcome back,
          <span className="font-semibold text-blue-600">{user?.name}</span></p>
      </section>

      {/* Teams Section */}
      <section className="bg-white rounded-lg shadow-md p-6">
       <div className="team_heading flex items-center justify-between mb-6 ">
         <h2 className="text-2xl font-bold text-gray-800 mb-6 border-b pb-3">
          Your Teams
        </h2>
        <button className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 " onClick={()=>createTeam()}>Add Team</button>
       </div>

         <ul className="grid gap-4" style={{ gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))' }}>
          {teams.map((team) => (
            <li key={team.teamId} className='transition-transform hover:scale-105 cursor-pointer' onClick={() => navigate(`/admin/team/${team.teamId}`)}>
              <TeamCard teamId={team.teamId} teamName={team.name} totalMembers={team.memberCount} inProgress={team.pendingTasks} totalTasks={team.totalTasks} completed={team.completedTasks} overdue={team.overdueTasks} />
            </li>
          ))}
        </ul>
      </section>
    </div>
  );
};