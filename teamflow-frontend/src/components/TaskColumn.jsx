import React from 'react'
import { TaskCard } from './TaskCard';
import { useDroppable } from '@dnd-kit/core';

export const TaskColumn = ({ column,tasks }) => {
    const { setNodeRef } = useDroppable({
        id: column.id,
    });

    return (
        <div className=" flex flex-1 flex-col rounded-lg bg-neutral-800 p-4">
             <h3 className="mb-4 font-semibold text-neutral-100 ">{column.title}</h3>
            <div ref={setNodeRef} className="flex flex-1 flex-col gap-4">
                 {tasks.map(task => (
                <TaskCard key={task.taskId} taskId={task.taskId} taskName={task.taskName} teamName={task.teamName} dueDate={task.dueDate} />
             ))}
            </div>
        </div>
    )
}
