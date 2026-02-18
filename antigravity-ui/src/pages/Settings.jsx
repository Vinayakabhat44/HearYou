import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import authService from '../services/authService';
import Button from '../components/Button';
import Input from '../components/Input';

const Settings = () => {
    const { user } = useAuth();
    const [preferences, setPreferences] = useState(user?.preferences || {});
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState('');

    const handleChange = (e) => {
        setPreferences({ ...preferences, [e.target.name]: e.target.value });
    };

    const savePreferences = async (e) => {
        e.preventDefault();
        setLoading(true);
        setMessage('');
        try {
            await authService.updatePreferences(user.id, preferences);
            setMessage('Preferences saved successfully!');
        } catch (err) {
            setMessage('Failed to save preferences.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="settings-page">
            <div className="glass-panel" style={{ padding: '2rem', borderRadius: '1.5rem' }}>
                <h2 style={{ color: 'var(--primary)', marginBottom: '2rem' }}>Settings</h2>

                {message && <div style={{ padding: '1rem', borderRadius: '0.5rem', background: message.includes('success') ? 'rgba(76, 175, 80, 0.1)' : 'rgba(244, 67, 54, 0.1)', color: message.includes('success') ? '#4caf50' : '#f44336', marginBottom: '1.5rem', textAlign: 'center' }}>{message}</div>}

                <section style={{ marginBottom: '3rem' }}>
                    <h3 style={{ marginBottom: '1.5rem' }}>User Preferences</h3>
                    <form onSubmit={savePreferences}>
                        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1.5rem' }}>
                            <Input
                                label="Theme"
                                name="theme"
                                value={preferences.theme || 'dark'}
                                onChange={handleChange}
                                placeholder="dark or light"
                            />
                            <Input
                                label="Language"
                                name="lang"
                                value={preferences.lang || 'en'}
                                onChange={handleChange}
                                placeholder="e.g. en, kn, hi"
                            />
                        </div>
                        <Button type="submit" disabled={loading} style={{ marginTop: '1rem' }}>
                            {loading ? 'Saving...' : 'Save Preferences'}
                        </Button>
                    </form>
                </section>

                <section>
                    <h3 style={{ marginBottom: '1.5rem' }}>Account Details</h3>
                    <div className="glass-panel" style={{ padding: '1.5rem', borderRadius: '1rem', display: 'flex', flexDirection: 'column', gap: '0.8rem' }}>
                        <p><strong>Username:</strong> {user?.username}</p>
                        <p><strong>Email:</strong> {user?.email}</p>
                        <p><strong>Location:</strong> {user?.location || 'Not set'}</p>
                    </div>
                </section>
            </div>
        </div>
    );
};

export default Settings;
