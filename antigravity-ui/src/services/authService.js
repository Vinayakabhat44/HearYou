import axios from 'axios';

const API_REF = '/api/auth';
const USER_REF = '/api/users';

const login = async (username, password) => {
    const response = await axios.post(`${API_REF}/login`, { username, password });
    if (response.data.token) {
        localStorage.setItem('user', JSON.stringify(response.data));
    }
    return response.data;
};

const register = async (userData) => {
    const response = await axios.post(`${API_REF}/register`, userData);
    return response.data;
};

const logout = () => {
    localStorage.removeItem('user');
};

const getCurrentUser = () => {
    return JSON.parse(localStorage.getItem('user'));
};

const getUserProfile = async (userId) => {
    const response = await axios.get(`${USER_REF}/${userId}`);
    return response.data;
};

const updateLocation = async (userId, lat, lng) => {
    const response = await axios.put(`${USER_REF}/${userId}/location?lat=${lat}&lng=${lng}`);
    return response.data;
};

const updatePreferences = async (userId, preferences) => {
    const user = getCurrentUser();
    const response = await axios.put(`${USER_REF}/${userId}/preferences`, preferences, {
        headers: { Authorization: `Bearer ${user.token}` }
    });
    return response.data;
};

export default {
    login,
    register,
    logout,
    getCurrentUser,
    getUserProfile,
    updateLocation,
    updatePreferences
};
