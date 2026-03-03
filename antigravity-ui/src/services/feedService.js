import apiClient from './apiClient';

const getHierarchicalFeed = async () => {
    const response = await apiClient.get('/api/feed/hierarchical');
    return response.data;
};

const getFriendsFeed = async () => {
    const response = await apiClient.get('/api/feed/friends');
    return response.data;
};

const createStory = async (story, file, lat, lng) => {
    const formData = new FormData();
    formData.append('story', new Blob([JSON.stringify(story)], { type: 'application/json' }));
    if (file) {
        formData.append('file', file);
    }

    let url = '/api/stories';
    const params = new URLSearchParams();
    if (lat) params.append('lat', lat);
    if (lng) params.append('lng', lng);
    if (params.toString()) {
        url += `?${params.toString()}`;
    }

    const response = await apiClient.post(url, formData, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    });
    return response.data;
};

export default {
    getHierarchicalFeed,
    getFriendsFeed,
    createStory,
};
