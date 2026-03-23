import React, { createContext, useState, useEffect, useContext } from 'react';
import authService from '../services/authService';

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const savedUser = authService.getCurrentUser();
        if (savedUser) {
            setUser(savedUser);
        }
        setLoading(false);
    }, []);

    const login = async (username, password) => {
        const data = await authService.login(username, password);
        setUser(data);
        return data;
    };

    const register = async (userData) => {
        const data = await authService.register(userData);
        if (data.token) {
            setUser(data);
            localStorage.setItem('user', JSON.stringify(data));
        }
        return data;
    };

    const logout = () => {
        authService.logout();
        setUser(null);
    };

    const value = {
        user,
        loading,
        login,
        register,
        logout,
        reverseGeocode: authService.reverseGeocode
    };

    return (
        <AuthContext.Provider value={value}>
            {!loading && children}
        </AuthContext.Provider>
    );
};
