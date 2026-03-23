import apiClient from './apiClient';

const getLocalNews = async (category = null) => {
    const userJson = localStorage.getItem('user');
    const user = userJson ? JSON.parse(userJson) : null;
    
    const params = {
        category,
        pincode: user?.pincode,
        taluk: user?.taluk,
        district: user?.district,
        state: user?.state
    };

    // Clean up empty params
    Object.keys(params).forEach(key => (params[key] == null || params[key] === '') && delete params[key]);

    const response = await apiClient.get('/api/news/local-feed', { params });
    return response.data;
};

export default {
    getLocalNews,
};
