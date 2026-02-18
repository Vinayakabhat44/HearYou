import apiClient from './apiClient';

const getHierarchicalFeed = async () => {
    const response = await apiClient.get('/api/feed/hierarchical');
    return response.data;
};

const getFriendsFeed = async () => {
    const response = await apiClient.get('/api/feed/friends');
    return response.data;
};

export default {
    getHierarchicalFeed,
    getFriendsFeed,
};
