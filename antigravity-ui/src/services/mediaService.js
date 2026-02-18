import apiClient from './apiClient';

const uploadMedia = async (file, folder = 'posts') => {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('folder', folder);

    const response = await apiClient.post('/api/media/upload', formData, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    });
    return response.data;
};

const getFileUrl = (folder, fileName) => {
    return `/api/media/files/${folder}/${fileName}`;
};

export default {
    uploadMedia,
    getFileUrl,
};
