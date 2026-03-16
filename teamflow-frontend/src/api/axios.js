import React from 'react'
import axios from "axios"
import { jwtDecode } from 'jwt-decode';


const API = axios.create({
    baseURL : import.meta.env.VITE_API_URL,
    withCredentials : true
});

API.interceptors.request.use((config) => {
  const token = localStorage.getItem("token")

  console.log('Request config:', config);
  console.log('Token from localStorage:', token);
  if (token) {
    const decoded = jwtDecode(token);
    const currentTime = Date.now() / 1000;
    if (decoded.exp < currentTime) {
      console.log("Token expired, removing from localStorage");
      localStorage.removeItem("token");
      return Promise.reject(new Error("Token expired"));
    }
    config.headers.Authorization = `Bearer ${token}`
  }

  return config
})

export default API;
