import axios from 'axios';
import authService from './authService';

const apiClient = axios.create({
    baseURL: '/', // Uses Vite proxy
    headers: {
        'Content-Type': 'application/json',
    },
});

// Interceptor to add Auth Token to every request
apiClient.interceptors.request.use(
    (config) => {
        const user = authService.getCurrentUser();
        if (user && user.token) {
            config.headers.Authorization = `Bearer ${user.token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Interceptor to handle unauthorized errors (401)
apiClient.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response && error.response.status === 401) {
            authService.logout();
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);

export default apiClient;
