import React, { useState, useEffect } from 'react';
import socialService from '../services/socialService';
import { useAuth } from '../context/AuthContext';
import Button from '../components/Button';
import Input from '../components/Input';

const Group = () => {
    const { user } = useAuth();
    const [friends, setFriends] = useState([]);
    const [pending, setPending] = useState([]);
    const [searchQuery, setSearchQuery] = useState('');
    const [searchResults, setSearchResults] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            if (!user) return;
            setLoading(true);
            try {
                const [friendsList, pendingList] = await Promise.all([
                    socialService.getFriendsList(user.id).catch(() => []),
                    socialService.getPendingRequests(user.id).catch(() => [])
                ]);
                setFriends(friendsList || []);
                setPending(pendingList || []);
            } catch (err) {
                console.error('Failed to fetch social data:', err);
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, [user]);

    const handleSearch = async (e) => {
        e.preventDefault();
        if (!searchQuery.trim()) return;
        try {
            const results = await socialService.searchUsers(searchQuery);
            setSearchResults(results || []);
        } catch (err) {
            console.error('Search failed:', err);
        }
    };

    const sendRequest = async (userId) => {
        try {
            await socialService.sendFriendRequest(userId);
            alert('Friend request sent!');
        } catch (err) {
            alert('Failed to send friend request.');
        }
    };

    return (
        <div className="group-page">
            <div className="glass-panel" style={{ padding: '2rem', borderRadius: '1.5rem' }}>
                <h2 style={{ color: 'var(--primary)', marginBottom: '2rem' }}>Your Group</h2>

                {/* Search Section */}
                <section style={{ marginBottom: '3rem' }}>
                    <h3 style={{ marginBottom: '1rem' }}>Find People</h3>
                    <form onSubmit={handleSearch} style={{ display: 'flex', gap: '1rem', alignItems: 'flex-end' }}>
                        <div style={{ flex: 1 }}>
                            <Input
                                label="Search"
                                placeholder="Search by username or name..."
                                value={searchQuery}
                                onChange={(e) => setSearchQuery(e.target.value)}
                            />
                        </div>
                        <Button type="submit" style={{ height: '48px', marginBottom: '1.5rem' }}>Search</Button>
                    </form>

                    {searchResults.length > 0 && (
                        <div style={{ marginTop: '1rem', display: 'grid', gap: '1rem' }}>
                            {searchResults.map(result => (
                                <div key={result.id} className="glass-panel" style={{ padding: '1rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderRadius: '1rem' }}>
                                    <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
                                        <div className="avatar">{result.username?.[0]?.toUpperCase()}</div>
                                        <span>{result.username}</span>
                                    </div>
                                    <Button variant="secondary" onClick={() => sendRequest(result.id)}>Add Friend</Button>
                                </div>
                            ))}
                        </div>
                    )}
                </section>

                {/* Connections Section */}
                <section>
                    <h3 style={{ marginBottom: '1rem' }}>My Connections</h3>
                    {loading ? (
                        <p>Loading your network...</p>
                    ) : friends.length > 0 ? (
                        <div style={{ display: 'grid', gap: '1rem' }}>
                            {friends.map(friend => (
                                <div key={friend.id} className="glass-panel" style={{ padding: '1rem', display: 'flex', alignItems: 'center', gap: '1rem', borderRadius: '1rem' }}>
                                    <div className="avatar">{friend.username?.[0]?.toUpperCase()}</div>
                                    <span>{friend.username}</span>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <p style={{ color: 'var(--text-muted)' }}>You haven't added any friends yet.</p>
                    )}
                </section>
            </div>
        </div>
    );
};

export default Group;
