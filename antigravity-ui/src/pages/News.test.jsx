import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import News from './News';
import newsService from '../services/newsService';

// Mocking the newsService and TabScroll component
vi.mock('../services/newsService');
vi.mock('../components/TabScroll', () => ({
    default: ({ tabs, activeTab, onTabChange }) => (
        <div data-testid="tab-scroll">
            {tabs.map(tab => (
                <button key={tab} onClick={() => onTabChange(tab)}>{tab}</button>
            ))}
        </div>
    )
}));

describe('News Component', () => {
    beforeEach(() => {
        vi.resetAllMocks();
    });

    it('renders loading state initially', async () => {
        newsService.getLocalNews.mockReturnValue(new Promise(() => { })); // Never resolves
        render(<News />);
        expect(screen.getByText(/Fetching trending news updates.../i)).toBeInTheDocument();
    });

    it('displays news items when API returns data', async () => {
        const mockResponse = {
            feed: {
                national: [
                    { title: 'Headline 1', source_id: 'BBC', description: 'Summary 1', link: 'http://news.com/1' }
                ]
            }
        };
        newsService.getLocalNews.mockResolvedValue(mockResponse);

        render(<News />);

        await waitFor(() => {
            expect(screen.getByText('Headline 1')).toBeInTheDocument();
            expect(screen.getByText(/BBC/)).toBeInTheDocument();
            expect(screen.getByText('Summary 1')).toBeInTheDocument();
        });
    });

    it('handles null response from API gracefully', async () => {
        newsService.getLocalNews.mockResolvedValue(null);

        render(<News />);

        await waitFor(() => {
            expect(screen.getByText(/No news found for "Trending News"/i)).toBeInTheDocument();
        });
    });

    it('handles empty feed object from API gracefully', async () => {
        newsService.getLocalNews.mockResolvedValue({ feed: {} });

        render(<News />);

        await waitFor(() => {
            expect(screen.getByText(/No news found for "Trending News"/i)).toBeInTheDocument();
        });
    });

    it('handles API error gracefully', async () => {
        const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => { });
        newsService.getLocalNews.mockRejectedValue(new Error('API Failure'));

        render(<News />);

        await waitFor(() => {
            expect(screen.getByText(/No news found for "Trending News"/i)).toBeInTheDocument();
            expect(consoleSpy).toHaveBeenCalledWith('Failed to fetch news:', any());
        });
        consoleSpy.mockRestore();
    });

    it('switches tabs and fetches new data', async () => {
        newsService.getLocalNews.mockResolvedValue({ feed: { national: [] } });

        render(<News />);

        const localTab = screen.getByText('Local');
        fireEvent.click(localTab);

        await waitFor(() => {
            expect(newsService.getLocalNews).toHaveBeenCalledWith('local');
        });
    });

    it('renders fallback for missing fields in news items', async () => {
        const mockResponse = {
            feed: {
                national: [
                    { title: 'Headline 1', source_id: 'BBC', description: null, link: null }
                ]
            }
        };
        newsService.getLocalNews.mockResolvedValue(mockResponse);

        render(<News />);

        await waitFor(() => {
            expect(screen.getByText('Headline 1')).toBeInTheDocument();
            const cards = screen.getAllByClassName('news-card');
            expect(cards[0]).toBeInTheDocument();
            // Should not crash due to null description/link
        });
    });
});
