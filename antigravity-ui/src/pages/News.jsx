import React, { useState, useEffect } from 'react';
import newsService from '../services/newsService';
import TabScroll from '../components/TabScroll';
import './News.css';

const News = () => {
    const tabs = [
        'Trending News', 'Local', 'Taluk', 'District',
        'State', 'Nation', 'International', 'Health',
        'Sports', 'Business and Finance', 'Entertainment'
    ];
    const [activeTab, setActiveTab] = useState('Trending News');
    const [news, setNews] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchNews = async () => {
            setLoading(true);
            try {
                // If 'Trending News' is selected, fetch the general feed (no category)
                const categoryParam = activeTab === 'Trending News' ? null : activeTab.toLowerCase();
                const data = await newsService.getLocalNews(categoryParam);
                setNews(data || []);
            } catch (err) {
                console.error('Failed to fetch news:', err);
                setNews([]);
            } finally {
                setLoading(false);
            }
        };
        fetchNews();
    }, [activeTab]);

    return (
        <div className="news-page">
            <TabScroll
                tabs={tabs}
                activeTab={activeTab}
                onTabChange={setActiveTab}
            />

            <div className="news-container glass-panel">
                <div className="news-header">
                    <h2>{activeTab} Updates</h2>
                </div>

                {loading ? (
                    <p>Fetching {activeTab.toLowerCase()} updates...</p>
                ) : news.length > 0 ? (
                    <div className="news-grid">
                        {news.map((item, index) => (
                            <div key={item.id || index} className="glass-panel news-card">
                                <h3>{item.title}</h3>
                                <p className="news-meta">{item.source} • {item.publishedDate || 'Today'}</p>
                                <p className="news-summary">{item.summary || item.content}</p>
                                {item.url && (
                                    <a
                                        href={item.url}
                                        target="_blank"
                                        rel="noopener noreferrer"
                                        className="read-more-link"
                                    >
                                        Read More
                                    </a>
                                )}
                            </div>
                        ))}
                    </div>
                ) : (
                    <p style={{ color: 'var(--text-muted)' }}>No news found for "{activeTab}". Check back later!</p>
                )}
            </div>
        </div>
    );
};

export default News;
