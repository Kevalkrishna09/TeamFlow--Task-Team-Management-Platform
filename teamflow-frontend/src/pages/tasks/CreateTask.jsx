import React from 'react'
import { task } from '../../api/task'
export const CreateTask = ({ setCreateTaskModalOpen, members, teamId, setTaskCreated }) => {
  const [formData, setFormData] = React.useState({
    title: '',
    description: '',
    priority: 'LOW',
    dueDate: '',
    assignedUserId: ''
  })
  console.log("members in create task " + teamId);

  const handleSubmit = async (e) => {
    e.preventDefault()
    console.log("formData " + formData.title);
    console.log("formData " + formData.description);
    try {
      const response = await task(teamId, formData);
      console.log("Task created successfully ", response.data);
     setTaskCreated(prev => prev + 1);
      setCreateTaskModalOpen(false);
    }
    catch (error) {
      console.log("Error creating task ", error);

      window.alert("Task creation failed. Please try again.");


    }
    finally {
      setCreateTaskModalOpen(false);
      setTaskCreated(prev => prev + 1);
    }
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-40">
      <div className="bg-white rounded-lg shadow-lg p-6 w-full max-w-md relative">

        <h2 className="text-xl font-bold mb-4">Add Task</h2>
        <form className="max-w-sm mx-auto space-y-4 bg-white p-6 rounded-lg shadow-md"
          onSubmit={handleSubmit}
        >
          <div
            className="absolute top-4 right-4 w-6 h-6 flex items-center justify-center cursor-pointer"
            onClick={() => setCreateTaskModalOpen(false)}
          >
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640"><path d="M183.1 137.4C170.6 124.9 150.3 124.9 137.8 137.4C125.3 149.9 125.3 170.2 137.8 182.7L275.2 320L137.9 457.4C125.4 469.9 125.4 490.2 137.9 502.7C150.4 515.2 170.7 515.2 183.2 502.7L320.5 365.3L457.9 502.6C470.4 515.1 490.7 515.1 503.2 502.6C515.7 490.1 515.7 469.8 503.2 457.3L365.8 320L503.1 182.6C515.6 170.1 515.6 149.8 503.1 137.3C490.6 124.8 470.3 124.8 457.8 137.3L320.5 274.7L183.1 137.4z" /></svg>
          </div>

          <div>
            <label htmlFor="title" className="block mb-2.5 text-sm font-medium text-heading">Title</label>
            <input type="text" id="title" className="bg-neutral-secondary-medium border border-default-medium text-heading text-sm rounded-base focus:ring-brand focus:border-brand block w-full px-2.5 py-2 shadow-xs placeholder:text-body" placeholder="" required value={formData.title} onChange={(e) => setFormData({ ...formData, title: e.target.value })} />
          </div>

          <div>
            <label htmlFor="description" className="block mb-2.5 text-sm font-medium text-heading">Description</label>
            <textarea id="description" className="bg-neutral-secondary-medium border border-default-medium text-heading text-sm rounded-base focus:ring-brand focus:border-brand block w-full px-2.5 py-2 shadow-xs placeholder:text-body" placeholder="" required value={formData.description} onChange={(e) => setFormData({ ...formData, description: e.target.value })} />
          </div>

          <div>
            <label htmlFor="priority" className="block mb-2.5 text-sm font-medium text-heading">Priority</label>
            <select id="priority" name="priority" className="bg-neutral-secondary-medium border border-default-medium text-heading text-sm rounded-base focus:ring-brand focus:border-brand block w-full px-2.5 py-2 shadow-xs placeholder:text-body" value={formData.priority} onChange={(e) => setFormData({ ...formData, priority: e.target.value })}>
              <option value="LOW">Low</option>
              <option value="MEDIUM">Medium</option>
              <option value="HIGH">High</option>
            </select>
          </div>

          <div>
            <label htmlFor="dueDate" className="block mb-2.5 text-sm font-medium text-heading">Due Date</label>
            <input type="date" id="dueDate" className="bg-neutral-secondary-medium border border-default-medium text-heading text-sm rounded-base focus:ring-brand focus:border-brand block w-full px-2.5 py-2 shadow-xs placeholder:text-body" required value={formData.dueDate} onChange={(e) => setFormData({ ...formData, dueDate: e.target.value })} />
          </div>

          <div>
            <label htmlFor="assignee" className="block mb-2.5 text-sm font-medium text-heading">Assignee</label>

            <select id="assignee" value={formData.assignedUserId} onChange={(e) => setFormData({ ...formData, assignedUserId: e.target.value })} className="bg-neutral-secondary-medium border border-default-medium text-heading text-sm rounded-base focus:ring-brand focus:border-brand block w-full px-2.5 py-2 shadow-xs placeholder:text-body">
              <option value="">Select Assignee</option>

              {members?.data?.data.map((member) => (
                <option key={member.id} value={member.memberEmail}>{member.memberName}</option>
              ))}
            </select>
          </div>


          <button type="submit" className="text-white bg-blue-500 hover:bg-blue-600 focus:ring-4 focus:ring-blue-300 font-medium rounded-base text-sm w-full px-5 py-2.5 text-center"  >Add Task</button>
        </form>

      </div>
    </div>
  )
}


