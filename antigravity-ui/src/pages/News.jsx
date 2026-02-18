import React, { useState, useEffect } from 'react';
import newsService from '../services/newsService';

const News = () => {
    const [news, setNews] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchNews = async () => {
            try {
                const data = await newsService.getLocalNews();
                setNews(data || []);
            } catch (err) {
                console.error('Failed to fetch news:', err);
            } finally {
                setLoading(false);
            }
        };
        fetchNews();
    }, []);

    return (
        <div className="news-page">
            <div className="glass-panel" style={{ padding: '2rem', borderRadius: '1.5rem' }}>
                <h2 style={{ color: 'var(--primary)', marginBottom: '2rem' }}>Latest News</h2>

                {loading ? (
                    <p>Fetching local updates...</p>
                ) : news.length > 0 ? (
                    <div style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
                        {news.map((item, index) => (
                            <div key={item.id || index} className="glass-panel" style={{ padding: '1.5rem', borderRadius: '1rem' }}>
                                <h3 style={{ marginBottom: '0.5rem' }}>{item.title}</h3>
                                <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', marginBottom: '1rem' }}>{item.source} • {item.publishedDate || 'Today'}</p>
                                <p>{item.summary || item.content}</p>
                                {item.url && <a href={item.url} target="_blank" rel="noopener noreferrer" style={{ display: 'inline-block', marginTop: '1rem', color: 'var(--primary)', fontWeight: '600' }}>Read More</a>}
                            </div>
                        ))}
                    </div>
                ) : (
                    <p style={{ color: 'var(--text-muted)' }}>No news found for your area. Check back later!</p>
                )}
            </div>
        </div>
    );
};

export default News;
