import apiClient from './apiClient';

const searchUsers = async (query) => {
    const response = await apiClient.get(`/api/users/search?query=${query}`);
    return response.data;
};

const getFriendsList = async (userId) => {
    const response = await apiClient.get(`/api/social/friends/${userId}/list`);
    return response.data;
};

const getPendingRequests = async (userId) => {
    const response = await apiClient.get(`/api/social/friends/${userId}/pending`);
    return response.data;
};

const sendFriendRequest = async (targetUserId) => {
    const response = await apiClient.post('/api/social/friends/request', { targetUserId });
    return response.data;
};

const respondToRequest = async (requestId, status) => {
    const response = await apiClient.put(`/api/social/friends/${requestId}/respond?status=${status}`);
    return response.data;
};

export default {
    searchUsers,
    getFriendsList,
    getPendingRequests,
    sendFriendRequest,
    respondToRequest,
};
