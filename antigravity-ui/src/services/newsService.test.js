import { describe, it, expect, vi, beforeEach } from 'vitest';
import newsService from './newsService';
import apiClient from './apiClient';

vi.mock('./apiClient');

describe('newsService', () => {
    beforeEach(() => {
        vi.resetAllMocks();
    });

    it('getLocalNews calls the correct endpoint without category', async () => {
        apiClient.get.mockResolvedValue({ data: { feed: {} } });

        await newsService.getLocalNews();

        expect(apiClient.get).toHaveBeenCalledWith('/api/news/local-feed', { params: {} });
    });

    it('getLocalNews calls the correct endpoint with category', async () => {
        apiClient.get.mockResolvedValue({ data: { feed: {} } });

        await newsService.getLocalNews('district');

        expect(apiClient.get).toHaveBeenCalledWith('/api/news/local-feed', { params: { category: 'district' } });
    });

    it('handles API errors by throwing', async () => {
        apiClient.get.mockRejectedValue(new Error('Network error'));

        await expect(newsService.getLocalNews()).rejects.toThrow('Network error');
    });

    it('handles unexpected null response data', async () => {
        apiClient.get.mockResolvedValue({ data: null });

        const data = await newsService.getLocalNews();
        expect(data).toBeNull();
    });
});
