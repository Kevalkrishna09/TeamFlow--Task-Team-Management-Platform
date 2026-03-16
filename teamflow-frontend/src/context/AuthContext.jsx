import React from 'react'
import { useContext, useState, createContext, useEffect } from 'react'
import { jwtDecode } from 'jwt-decode'

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {


    const [user, setUser] = useState(null);
    const [loading,setLoading] = useState(true);
    const isTokenValid = (token) =>{
        if(!token) return false;
        try{
            const decoded = jwtDecode(token);
            const currentTime = Date.now() / 1000;
            return decoded.exp > currentTime;
        } catch (error) {
            return false;
        }
    }
    useEffect(() => {
        const token = localStorage.getItem("token");

        if (token && isTokenValid(token)) {
            try {
                const decoded = jwtDecode(token);
                setUser(decoded);
            } catch (error) {
                console.error("Invalid token");
                localStorage.removeItem("token");
            }
        }
        else{
            localStorage.removeItem("token");
            setUser(null);
        }

        setLoading(false);
    }, []);


    const login = (token) => {
        console.log("inside authContext login user");
        localStorage.setItem("token", token);
        const decode = jwtDecode(token);
        console.log("decoded token",decode)
        setUser(decode);
        return decode;
    }
    const signup = (token) => {
        localStorage.setItem("token", token);
        const decode = jwtDecode(token);
        setUser(decode);
    }

    const logout = () => {
        localStorage.removeItem("token");
        setUser(null);
        
    }
    return (
        <AuthContext.Provider value={{ user, login, logout,signup,loading }}>
            {!loading && children}
        </AuthContext.Provider>
    );
}
export const useAuth = ()=> useContext(AuthContext);