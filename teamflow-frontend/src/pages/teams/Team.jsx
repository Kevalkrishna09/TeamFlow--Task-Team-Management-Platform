//fetch the team from backend , it will fetch all the team members , all the tasks , how many task are assiged to a member , kind of taks assgined to a member 
import React, { useEffect } from 'react'
import { getTeam, teamMembers, teamTasks } from '../../api/team';
import { deleteTask } from '../../api/task';
import { useParams } from 'react-router-dom';
import TeamCard from '../../components/TeamCard';
import { CreateMember } from '../members/CreateMember';
import { CreateTask } from '../tasks/CreateTask';
import { task } from '../../api/task';
export const Team = () => {
    const { teamId } = useParams();
    const [team, setTeam] = React.useState(null);
    const [members, setMembers] = React.useState([]);
    const [tasks, setTasks] = React.useState([]);
    const [createMemberModalOpen, setCreateMemberModalOpen] = React.useState(false);
    const [createTaskModalOpen, setCreateTaskModalOpen] = React.useState(false);
    const [taskCreated, setTaskCreated] = React.useState(1);
    //fetch team count using teamId

    useEffect(() => {
        const fetchTeam = async () => {
            try {

                const data = await getTeam(teamId);
                console.log('Fetched team details:', data);
                setTeam(data);
            } catch (err) {
                console.error('Error fetching team details:', err);
            }
        };

        const fetchTeamMembers = async () => {
            try {

                const data = await teamMembers(teamId);
                console.log('Fetched team members:', data);
                setMembers(data);
            }
            catch (err) {
                console.error('Error fetching team members ', err);
            }
        }

       

        fetchTeam();
        fetchTeamMembers();
      
    }, [taskCreated]);
    useEffect(() => {
         const fetchTeamTasks = async () => {
            try {
                console.log("Fetching team tasks for teamId: ", teamId);
                const response = await teamTasks(teamId);
             
                const fetchedTask = response.data.data;
                    console.log("Fetched team tasks: ", fetchedTask);
                setTasks(fetchedTask);
            } catch (err) {
                console.error('Error fetching team tasks ', err);
            }
        }
          fetchTeamTasks();
    },[taskCreated]);
    if (!team) return <div>Loading...</div>;
    //fetch team details using teamId 
    //fetch team members using teamId
    //fetch tasks using teamId

    const createMember = () => {
        console.log("create member function called");
        setCreateMemberModalOpen(true);
       
    }
    const handleCreateTask = () => {
        console.log("create task function called");
        setCreateTaskModalOpen(true);
    }

    const handleRemoveTask = async (task) => {
        try {
            console.log("Removing task with taskId: ", task.taskId);
            await deleteTask(task.taskId);
            setTasks((prevTasks) => prevTasks.filter((t) => t.taskId !== task.taskId));
        } catch (error) {
            console.error('Error removing task:', error);
        }
    }
    return (
        <div>
            <section className="team-section bg-white rounded-lg shadow-md p-6 mb-8">
                <TeamCard teamName={team.name} totalMembers={team.memberCount} inProgress={team.pendingTasks} totalTasks={team.totalTasks} completed={team.completedTasks} overdue={team.overdueTasks} />
            </section>
            {createMemberModalOpen && <CreateMember setCreateMemberModalOpen={setCreateMemberModalOpen} teamId={teamId} />}
            {createTaskModalOpen && <CreateTask setCreateTaskModalOpen={setCreateTaskModalOpen} members={members} teamId={teamId}  setTaskCreated = {setTaskCreated}/>}
            <section className="member-section bg-white rounded-lg shadow-md p-6 mb-8">
                <div className="team_heading flex items-center justify-between mb-6 ">
                    <h2 className="text-2xl font-bold text-gray-800 mb-6 border-b pb-3">
                      Team Members
                    </h2>
                    <button className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 " onClick={() => createMember()}>Add Member</button>
                </div>

                <div className="relative overflow-x-auto bg-neutral-primary-soft shadow-xs rounded-base border border-default">
                    <table className="w-full text-sm text-left rtl:text-right text-body">
                        <thead className="text-sm text-body bg-neutral-secondary-medium border-b border-default-medium">
                            <tr>
                                <th scope="col" className="px-6 py-3 font-medium">
                                    Name
                                </th>
                                <th scope="col" className="px-6 py-3 font-medium">
                                    <div className="flex items-center">
                                        Email
                                        <a href="#">
                                            <svg className="w-4 h-4 ms-1" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" viewBox="0 0 24 24"><path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="m8 15 4 4 4-4m0-6-4-4-4 4" /></svg>
                                        </a>
                                    </div>
                                </th>
                                <th scope="col" className="px-6 py-3 font-medium">
                                    <div className="flex items-center">
                                        Role
                                        <a href="#">
                                            <svg className="w-4 h-4 ms-1" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" viewBox="0 0 24 24"><path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="m8 15 4 4 4-4m0-6-4-4-4 4" /></svg>
                                        </a>
                                    </div>
                                </th>
                                <th scope="col" className="px-6 py-3 font-medium">
                                    <div className="flex items-center">
                                        Tasks
                                        <a href="#">
                                            <svg className="w-4 h-4 ms-1" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" viewBox="0 0 24 24"><path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="m8 15 4 4 4-4m0-6-4-4-4 4" /></svg>
                                        </a>
                                    </div>
                                </th>
                                <th scope="col" className="px-6 py-3 font-medium">
                                    <span className="sr-only">Remove</span>
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            {members?.data?.data.map((member) => (
                                <tr key={member.id} className="bg-neutral-primary-soft border-b border-default">
                                    <th scope="row" className="px-6 py-4 font-medium text-heading whitespace-nowrap">
                                        {member.memberName}
                                    </th>
                                    <td className="px-6 py-4">
                                        {member.memberEmail}
                                    </td>
                                    <td className="px-6 py-4">
                                        {member.memberRole}
                                    </td>
                                    <td className="px-6 py-4">
                                        {member.totalTasks}
                                    </td>
                                    <td className="px-6 py-4 text-right">
                                        <button onClick={() => handleRemoveMember(member.id)}>Remove</button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>

            </section>

 

            <section className="task-section bg-white rounded-lg shadow-md p-6 mb-8">
                   <div className="team_heading flex items-center justify-between mb-6 ">
                    
                    <h2 className="text-2xl font-bold text-gray-800 mb-6 border-b pb-3">
                      Team Tasks
                    </h2>
                    <button className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 " onClick={() => handleCreateTask()}>Add Task</button>
                </div>

                <div className="relative overflow-x-auto bg-neutral-primary-soft shadow-xs rounded-base border border-default">
                    <table className="w-full text-sm text-left rtl:text-right text-body">
                        <thead className="text-sm text-body bg-neutral-secondary-medium border-b border-default-medium">
                            <tr>
                                <th scope="col" className="px-6 py-3 font-medium">
                                    Task Name
                                </th>
                                <th scope="col" className="px-6 py-3 font-medium">
                                    <div className="flex items-center">
                                        Status 
                                        <a href="#">
                                            <svg className="w-4 h-4 ms-1" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" viewBox="0 0 24 24"><path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="m8 15 4 4 4-4m0-6-4-4-4 4" /></svg>
                                        </a>
                                    </div>
                                </th>
                                <th scope="col" className="px-6 py-3 font-medium">
                                    <div className="flex items-center">
                                        Priority
                                        <a href="#">
                                            <svg className="w-4 h-4 ms-1" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" viewBox="0 0 24 24"><path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="m8 15 4 4 4-4m0-6-4-4-4 4" /></svg>
                                        </a>
                                    </div>
                                </th>
                                <th scope="col" className="px-6 py-3 font-medium">
                                    <div className="flex items-center">
                                        Assignee
                                        <a href="#">
                                            <svg className="w-4 h-4 ms-1" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" viewBox="0 0 24 24"><path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="m8 15 4 4 4-4m0-6-4-4-4 4" /></svg>
                                        </a>
                                    </div>
                                </th>
                                <th scope="col" className="px-6 py-3 font-medium">
                                    <span className="sr-only">Due Date</span>
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            {tasks?.map((task) => (
                                <tr key={task.taskId} className="bg-neutral-primary-soft border-b border-default">
                                    <th scope="row" className="px-6 py-4 font-medium text-heading whitespace-nowrap">
                                        {task.taskName}
                                    </th>
                                    <td className="px-6 py-4">
                                        {task.status}
                                    </td>
                                    <td className="px-6 py-4">
                                        {task.priority}
                                    </td>
                                    <td className="px-6 py-4">
                                        {task.assignedTo}
                                    </td>
                                    <td className="px-6 py-4 text-right">
                                        <button onClick={() => handleRemoveTask(task)}>Remove</button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </section>
        </div>
    )
}
