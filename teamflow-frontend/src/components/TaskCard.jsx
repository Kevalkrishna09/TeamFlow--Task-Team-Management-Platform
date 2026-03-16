import React from 'react'
import { useDraggable } from '@dnd-kit/core'
export const TaskCard = ({
  taskId = '',
    taskName = '',
    teamName = '',
    dueDate = '',
}) => {
   const {attributes,listeners,setNodeRef,transform} = useDraggable({
      id: taskId,
   });
   const transformStyle = transform?{
    transform :`translate( ${transform.x}px , ${transform.y}px)`
   }:undefined;
    
  return (
   
    
        <div 
        ref={setNodeRef}
        {...listeners}
        {...attributes}
        className="cursor-grab bg-neutral-700 rounded-lg shadow-sm hover:shadow-md p-4 m-2"
        style={transformStyle}>
            <h3 className="text-lg font-semibold text-neutral-100">{taskName}</h3>
            <p className="text-sm text-neutral-400">Team: {teamName}</p>
            <p className="text-sm text-neutral-400">Due: {dueDate}</p>
        </div>

 
  )
}
