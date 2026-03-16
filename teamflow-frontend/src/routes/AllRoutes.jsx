import React from 'react'
import { Routes, Route } from "react-router-dom"
import { Admin, Home, Login, PageNotFound, Signup, User ,CreateTeam,Team} from '../pages'
import { InvitationPage } from '../pages/InvitationPage'
export const AllRoutes = () => {
  return (
    <div>
      <Routes>
        <Route path="/invitation" element={<InvitationPage />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/home" element={<Home />} />
        <Route path="/admin" element={<Admin />} />
        <Route path="/admin/team" element={<CreateTeam />} />
        <Route path="/admin/team/:teamId" element={<Team />} />
        <Route path="/user" element={<User />} />
        <Route path="*" element={<PageNotFound />} />
      </Routes>
    </div>
  )
}
