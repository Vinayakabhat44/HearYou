import React, { useState, useEffect } from 'react';
import feedService from '../services/feedService';
import './Stories.css';

const Stories = () => {
    const tabs = ['Friends', 'Local', 'Taluk', 'District', 'State', 'Nation', 'International'];
    const [activeTab, setActiveTab] = useState('Local');
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchFeed = async () => {
            setLoading(true);
            try {
                let data;
                if (activeTab === 'Friends') {
                    data = await feedService.getFriendsFeed();
                } else {
                    data = await feedService.getHierarchicalFeed();
                }
                setItems(data || []);
            } catch (err) {
                console.error('Failed to fetch feed:', err);
                setItems([]);
            } finally {
                setLoading(false);
            }
        };
        fetchFeed();
    }, [activeTab]);

    return (
        <div className="stories-page">
            <div className="tabs-container glass-panel">
                <div className="tabs-scroll">
                    {tabs.map(tab => (
                        <button
                            key={tab}
                            className={`tab-item ${activeTab === tab ? 'active' : ''}`}
                            onClick={() => setActiveTab(tab)}
                        >
                            {tab}
                        </button>
                    ))}
                </div>
            </div>

            <div className="feed-container">
                {loading ? (
                    <div className="glass-panel" style={{ padding: '2rem', textAlign: 'center' }}>Loading your stories...</div>
                ) : items.length > 0 ? (
                    items.map((item, index) => (
                        <div key={item.id || index} className="glass-panel feed-item">
                            <div className="item-header">
                                <div className="item-avatar">{item.author?.[0]?.toUpperCase() || '?'}</div>
                                <div className="item-meta">
                                    <h3>{item.author || 'Anonymous'}</h3>
                                    <span>{item.timestamp || 'Just now'} • {item.location || activeTab}</span>
                                </div>
                            </div>
                            <div className="item-content">
                                <p>{item.content || item.summary}</p>
                                {item.mediaUrl && <img src={item.mediaUrl} alt="post" style={{ width: '100%', borderRadius: '1rem', marginTop: '1rem' }} />}
                            </div>
                            <div className="item-actions">
                                <button className="action-btn">Like</button>
                                <button className="action-btn">Comment</button>
                                <button className="action-btn">Share</button>
                            </div>
                        </div>
                    ))
                ) : (
                    <div className="glass-panel" style={{ padding: '4rem', textAlign: 'center', borderRadius: '1.5rem' }}>
                        <h3>No stories here yet</h3>
                        <p style={{ color: 'var(--text-muted)', marginTop: '1rem' }}>Be the first to share something with your community!</p>
                    </div>
                )}
            </div>
        </div>
    );
};

export default Stories;
