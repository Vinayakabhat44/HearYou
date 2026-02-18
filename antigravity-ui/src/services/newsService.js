import apiClient from './apiClient';

const getLocalNews = async (params = {}) => {
    const response = await apiClient.get('/api/news/local-feed', { params });
    return response.data;
};

export default {
    getLocalNews,
};
