import React, { useEffect } from 'react'
import { useAuth } from '../context/AuthContext';
import { getUserTasksSummary, getUserTasks ,updateTaskStatus, getUserTeams} from '../api/user';
import { TaskColumn } from '../components/TaskColumn';
import { DndContext } from '@dnd-kit/core';

export const User = () => {
  const { user } = useAuth();
  const [taskSummary, setTaskSummary] = React.useState(null);
  const [tasks, setTasks] = React.useState(null);
  const [teams, setTeams] = React.useState(null);
  const fetchTaskSummary = async () => {
      console.log("Fetching task summary for userId: ", user.userId);
      const summary = await getUserTasksSummary(user.userId);
      setTaskSummary(summary);
      console.log("Task summary fetched: ", summary);

    }

    const fetchUserTasks = async () => {
      console.log("Fetching tasks for userId: ", user.userId);
      const fetchedTask = await getUserTasks(user.userId);
      setTasks(fetchedTask.data.tasks);
      console.log("User tasks fetched: ", fetchedTask.data.tasks);
    }

    const fetchUserTeams = async () => {
      console.log("Fetching teams for userId: ", user.userId);
      const response = await getUserTeams(user.userId);
      setTeams(response.data.teams);
      console.log("User teams fetched: ", response);
    }
  useEffect(() => {
    

    fetchTaskSummary();
    fetchUserTasks();
    fetchUserTeams();
  }, []);

  const Columns = [
    { id: 'TODO', title: 'To Do' },
    { id: 'IN_PROGRESS', title: 'In Progress' },
    { id: 'COMPLETED', title: 'Completed' },
  ]

  //dropEvent : DragEndEvent;
  const updateTaskAndCallTheApi = async (taskId, newStatus) => {
    console.log("Updating task status for taskId: ", taskId, " to new status: ", newStatus);
    await updateTaskStatus(user.userId, { taskId, status: newStatus });
    fetchTaskSummary();
  }

  const handleDrop = (dropEvent) => {
    const { active, over } = dropEvent;
    console.log("being called in handleDrop ");
    if (!over) return;
    const taskId = active.id;
    const newStatus = over.id;

    // Optimistically update UI
    setTasks((prevTasks) =>
      prevTasks.map(task =>
        task.taskId === taskId ? { ...task, status: newStatus } : task
      )
    );

    // Update backend and fetch summary
    updateTaskAndCallTheApi(taskId, newStatus);
  }

  return (
    <div>
      <section className="bg-white rounded-lg shadow-md p-6 mb-8">
        <h1 className="text-3xl font-bold text-gray-800 mb-2">Admin Dashboard</h1>
        <p className="text-gray-600 text-lg">Welcome back,
          <span className="font-semibold text-blue-600">{user?.name}</span></p>
        {/* task summary */}
        <div className="taskSummary mt-8">
          <h2 className="text-2xl font-semibold text-gray-800 mb-4">Task Summary</h2>
          {taskSummary ? (
            <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
              <div className="bg-blue-100 p-4 rounded-lg shadow  ">
                <h3 className="text-xl font-semibold text-blue-600">Total Tasks</h3>
                <p className="text-2xl font-bold text-gray-800">{taskSummary.data.totalTasks}</p>
              </div>
              <div className="bg-violet-100 p-4 rounded-lg shadow">
                <h3 className="text-xl font-semibold text-violet-600">In Progress</h3>
                <p className="text-2xl font-bold text-gray-800">{taskSummary.data.inProgressTasks}</p>
              </div>
              <div className="bg-yellow-100 p-4 rounded-lg shadow">
                <h3 className="text-xl font-semibold text-yellow-600">Overdue Tasks</h3>
                <p className="text-2xl font-bold text-gray-800">{taskSummary.data.overdueTasks}</p>
              </div>
              <div className="bg-green-100 p-4 rounded-lg shadow">
                <h3 className="text-xl font-semibold text-green-600">Completed Tasks</h3>
                <p className="text-2xl font-bold text-gray-800">{taskSummary.data.completedTasks}</p>
              </div>
            </div>
          ) : (
            <p className="text-gray-600">Loading task summary...</p>
          )}
        </div>
      </section>

      {/* tasks kanban table  */}
      <section className="bg-white rounded-lg shadow-md p-6 mb-8">
        <h2 className="text-2xl font-semibold text-gray-800 mb-4">My Tasks</h2>

        <div className="flex gap-4 ">
         <DndContext onDragEnd={handleDrop}>
           {tasks ? (
            Columns.map(column => (
              <TaskColumn
                key={column.id}
                column={column}
                tasks={tasks.filter(task => task.status === column.id)} />
            ))

          ) : (
            <p className="text-gray-600 flex justify-center items-center">Loading tasks...</p>
          )}
        </DndContext>
        </div>


      </section>

      {/* my teams */}
        <section className="bg-white rounded-lg shadow-md p-6 mb-8">
        <h2 className="text-2xl font-semibold text-gray-800 mb-4">My Teams</h2>
        {teams ? (
          <ul className="list-disc list-inside">
            {teams.map(team => (
              <li key={team.teamId} className="text-gray-700 text-lg">{team.teamName}</li>
            ))}
          </ul>
        ) : (
          <p className="text-gray-600">Loading teams...</p>
        )}
        </section>
    </div>
  )
}

