import React, { use } from 'react'
import { member } from '../../api/member';
export const CreateMember = ({ setCreateMemberModalOpen ,teamId}) => {

  const [formData, setFormData] = React.useState({
    email: '',
    role: 'MEMBER'
  })


  const handleSubmit = async (e) => {
    e.preventDefault()
    console.log("formData " + formData.email);
    console.log("formData " + formData.role);
    try {
      const response = await member(teamId, formData);
      console.log("Member created successfully ", response.data);
      setCreateMemberModalOpen(false);
      setCreateMemberModalOpen(false)
    }
    catch (error) {
      console.log("Error creating member ", error);
    }
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-40">
      <div className="bg-white rounded-lg shadow-lg p-6 w-full max-w-md relative">

        <h2 className="text-xl font-bold mb-4">Add Member</h2>
        <form className="max-w-sm mx-auto space-y-4 bg-white p-6 rounded-lg shadow-md"
          onSubmit={handleSubmit}
        >
          <div
            className="absolute top-4 right-4 w-6 h-6 flex items-center justify-center cursor-pointer"
            onClick={() => setCreateMemberModalOpen(false)}
          >
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640"><path d="M183.1 137.4C170.6 124.9 150.3 124.9 137.8 137.4C125.3 149.9 125.3 170.2 137.8 182.7L275.2 320L137.9 457.4C125.4 469.9 125.4 490.2 137.9 502.7C150.4 515.2 170.7 515.2 183.2 502.7L320.5 365.3L457.9 502.6C470.4 515.1 490.7 515.1 503.2 502.6C515.7 490.1 515.7 469.8 503.2 457.3L365.8 320L503.1 182.6C515.6 170.1 515.6 149.8 503.1 137.3C490.6 124.8 470.3 124.8 457.8 137.3L320.5 274.7L183.1 137.4z" /></svg>
          </div>

          <div>
            <label htmlFor="teamName" className="block mb-2.5 text-sm font-medium text-heading">Email</label>
            <input type="text" id="teamName" className="bg-neutral-secondary-medium border border-default-medium text-heading text-sm rounded-base focus:ring-brand focus:border-brand block w-full px-2.5 py-2 shadow-xs placeholder:text-body" placeholder="" required value={formData.email} onChange={(e) => setFormData({ ...formData, email: e.target.value })} />
          </div>

          <div>
            <label htmlFor="teamDescription" className="block mb-2.5 text-sm font-medium text-heading">Role</label>
            <select id="role" name="role" className="bg-neutral-secondary-medium border border-default-medium text-heading text-sm rounded-base focus:ring-brand focus:border-brand block w-full px-2.5 py-2 shadow-xs placeholder:text-body" value={formData.role} onChange={(e) => setFormData({ ...formData, role: e.target.value })}>
              <option value="TEAM_LEAD">TeamLead</option>
              <option value="MEMBER">Member</option>
          
            </select>
          </div>
          <button type="submit" className="text-white bg-blue-500 hover:bg-blue-600 focus:ring-4 focus:ring-blue-300 font-medium rounded-base text-sm w-full px-5 py-2.5 text-center"  >Add Member</button>
        </form>

      </div>
    </div>
  )
}


