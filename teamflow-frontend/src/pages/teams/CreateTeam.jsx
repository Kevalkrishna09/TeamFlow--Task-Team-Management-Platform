import React from 'react'
import { team } from '../../api/team';
import { useNavigate } from 'react-router-dom';
export const CreateTeam = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = React.useState({
      name: '',
      description: ''
    })

    const handleSubmit = async (e) => {
      e.preventDefault()
      console.log("formData " + formData.name);
      console.log("formData " + formData.description);
      try{
        const response = await team(formData);
        console.log("Team created successfully ", response.data);
        navigate("/admin");
      }
      catch(error){
        console.log("Error creating team ", error);
      }
    }

  return (
    <div className='py-10 border-black max-w-30'>
      <form className="max-w-sm mx-auto space-y-4"
      onSubmit={handleSubmit}
      >
        <div>
          <label htmlFor="teamName" className="block mb-2.5 text-sm font-medium text-heading">Team Name</label>
          <input type="text" id="teamName" className="bg-neutral-secondary-medium border border-default-medium text-heading text-sm rounded-base focus:ring-brand focus:border-brand block w-full px-2.5 py-2 shadow-xs placeholder:text-body" placeholder="" required  value={formData.name} onChange={(e) => setFormData({...formData, name: e.target.value})}/>
        </div>
        
        <div>
          <label htmlFor="teamDescription" className="block mb-2.5 text-sm font-medium text-heading">Team Description</label>
          <input type="text" id="teamDescription" className="bg-neutral-secondary-medium border border-default-medium text-heading text-base rounded-base focus:ring-brand focus:border-brand block w-full px-3.5 py-3 shadow-xs placeholder:text-body" placeholder="" required  value={formData.description} onChange={(e) => setFormData({...formData, description: e.target.value})}/>
        </div>
          <button type="submit" className="text-white bg-blue-500 hover:bg-blue-600 focus:ring-4 focus:ring-blue-300 font-medium rounded-base text-sm w-full px-5 py-2.5 text-center">Create Team</button>
      </form>
    </div>
  )
}
