import apiClient from './apiClient';

const getLocalNews = async (category = null) => {
    const params = category ? { category } : {};
    const response = await apiClient.get('/api/news/local-feed', { params });
    return response.data;
};

export default {
    getLocalNews,
};
