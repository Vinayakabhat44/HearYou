import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Button from '../components/Button';
import Input from '../components/Input';

const Register = () => {
    const [locationMode, setLocationMode] = useState('manual'); // 'manual' or 'auto'
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: '',
        mobileNumber: '',
        village: '',
        taluk: '',
        district: '',
        state: '',
        pincode: '',
        latitude: '',
        longitude: ''
    });
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [loading, setLoading] = useState(false);
    const { register, reverseGeocode } = useAuth();
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleGetLocation = () => {
        if (!navigator.geolocation) {
            setError('Geolocation is not supported by your browser');
            return;
        }

        setLoading(true);
        setError('');
        navigator.geolocation.getCurrentPosition(
            async (position) => {
                const lat = position.coords.latitude;
                const lng = position.coords.longitude;
                
                try {
                    const locationData = await reverseGeocode(lat, lng);
                    setFormData({
                        ...formData,
                        latitude: lat,
                        longitude: lng,
                        village: locationData.village || '',
                        taluk: locationData.taluk || '',
                        district: locationData.district || '',
                        state: locationData.state || '',
                        pincode: locationData.pincode || ''
                    });
                } catch (err) {
                    setError('Failed to fetch address details for your location');
                } finally {
                    setLoading(false);
                }
            },
            (err) => {
                setError('Unable to retrieve your location');
                setLoading(false);
            }
        );
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        const payload = {
            username: formData.username,
            email: formData.email,
            password: formData.password,
            mobileNumber: formData.mobileNumber
        };

        if (locationMode === 'manual') {
            payload.village = formData.village;
            payload.taluk = formData.taluk;
            payload.district = formData.district;
            payload.state = formData.state;
            payload.pincode = formData.pincode;
        } else {
            payload.latitude = formData.latitude;
            payload.longitude = formData.longitude;
            payload.village = formData.village;
            payload.taluk = formData.taluk;
            payload.district = formData.district;
            payload.state = formData.state;
            payload.pincode = formData.pincode;
        }

        try {
            await register(payload);
            setSuccess('Registration successful! Logging you in...');
            setTimeout(() => navigate('/'), 1500);
        } catch (err) {
            setError(err.response?.data?.message || 'Registration failed. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-page" style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '90vh', padding: '2rem' }}>
            <div className="glass-panel" style={{ padding: '2.5rem', width: '100%', maxWidth: '550px', borderRadius: '1.5rem' }}>
                <h2 style={{ marginBottom: '1.5rem', textAlign: 'center', color: 'var(--primary)' }}>Create Account</h2>

                {error && <div style={{ color: '#ff4d4d', marginBottom: '1rem', textAlign: 'center', backgroundColor: 'rgba(255, 77, 77, 0.1)', padding: '0.5rem', borderRadius: '0.5rem' }}>{error}</div>}
                {success && <div style={{ color: '#4caf50', marginBottom: '1rem', textAlign: 'center', backgroundColor: 'rgba(76, 175, 80, 0.1)', padding: '0.5rem', borderRadius: '0.5rem' }}>{success}</div>}

                <form onSubmit={handleSubmit}>
                    <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                        <Input label="Username" name="username" placeholder="Choose a username" value={formData.username} onChange={handleChange} required />
                        <Input label="Mobile Number" name="mobileNumber" placeholder="Enter mobile number" value={formData.mobileNumber} onChange={handleChange} required />
                    </div>

                    <Input label="Email (Optional)" name="email" type="email" placeholder="Enter your email" value={formData.email} onChange={handleChange} />

                    <Input label="Password" name="password" type="password" placeholder="Choose a password" value={formData.password} onChange={handleChange} required />

                    <div style={{ margin: '1.5rem 0' }}>
                        <label style={{ display: 'block', marginBottom: '0.8rem', fontSize: '0.9rem', color: 'var(--text-muted)' }}>Location Mode</label>
                        <div style={{ display: 'flex', gap: '2rem', padding: '0.5rem' }}>
                            <label style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', cursor: 'pointer' }}>
                                <input 
                                    type="radio" 
                                    name="locationMode" 
                                    value="manual" 
                                    checked={locationMode === 'manual'} 
                                    onChange={() => setLocationMode('manual')}
                                    style={{ accentColor: 'var(--primary)', width: '1.1rem', height: '1.1rem' }}
                                />
                                <span style={{ color: locationMode === 'manual' ? 'white' : 'var(--text-muted)' }}>Manual Address</span>
                            </label>
                            <label style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', cursor: 'pointer' }}>
                                <input 
                                    type="radio" 
                                    name="locationMode" 
                                    value="auto" 
                                    checked={locationMode === 'auto'} 
                                    onChange={() => {
                                        setLocationMode('auto');
                                        handleGetLocation();
                                    }}
                                    style={{ accentColor: 'var(--primary)', width: '1.1rem', height: '1.1rem' }}
                                />
                                <span style={{ color: locationMode === 'auto' ? 'white' : 'var(--text-muted)' }}>Use Current Location</span>
                            </label>
                        </div>
                    </div>

                    <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0.8rem' }}>
                        <Input label="Village" name="village" placeholder="Village" value={formData.village} onChange={handleChange} disabled={locationMode === 'auto'} />
                        <Input label="Taluk" name="taluk" placeholder="Taluk" value={formData.taluk} onChange={handleChange} disabled={locationMode === 'auto'} />
                        <Input label="District" name="district" placeholder="District" value={formData.district} onChange={handleChange} disabled={locationMode === 'auto'} />
                        <Input label="State" name="state" placeholder="State" value={formData.state} onChange={handleChange} disabled={locationMode === 'auto'} />
                        <div style={{ gridColumn: 'span 2' }}>
                            <Input label="Pincode" name="pincode" placeholder="Pincode" value={formData.pincode} onChange={handleChange} disabled={locationMode === 'auto'} />
                        </div>
                    </div>

                    <Button type="submit" style={{ width: '100%', marginTop: '1.5rem' }} disabled={loading}>
                        {loading ? 'Processing...' : 'Register Account'}
                    </Button>
                </form>

                <p style={{ marginTop: '1.5rem', textAlign: 'center', color: 'var(--text-muted)' }}>
                    Already have an account? <Link to="/login" style={{ color: 'var(--primary)' }}>Login</Link>
                </p>
            </div>
        </div>
    );
};

export default Register;
