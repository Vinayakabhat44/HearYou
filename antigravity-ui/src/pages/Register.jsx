import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Button from '../components/Button';
import Input from '../components/Input';

const Register = () => {
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: '',
        mobileNumber: '',
        location: ''
    });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const { register } = useAuth();
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);
        try {
            await register(formData);
            navigate('/login', { state: { message: 'Registration successful! Please login.' } });
        } catch (err) {
            setError(err.response?.data?.message || 'Registration failed. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-page" style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '90vh' }}>
            <div className="glass-panel" style={{ padding: '3rem', width: '100%', maxWidth: '450px', borderRadius: '1.5rem' }}>
                <h2 style={{ marginBottom: '2rem', textAlign: 'center', color: 'var(--primary)' }}>Create Account</h2>

                {error && <div style={{ color: '#ff4d4d', marginBottom: '1rem', textAlign: 'center' }}>{error}</div>}

                <form onSubmit={handleSubmit}>
                    <Input
                        label="Username"
                        name="username"
                        placeholder="Choose a username"
                        value={formData.username}
                        onChange={handleChange}
                        required
                    />
                    <Input
                        label="Email"
                        name="email"
                        type="email"
                        placeholder="Enter your email"
                        value={formData.email}
                        onChange={handleChange}
                        required
                    />
                    <Input
                        label="Mobile Number"
                        name="mobileNumber"
                        placeholder="Enter your mobile number"
                        value={formData.mobileNumber}
                        onChange={handleChange}
                    />
                    <Input
                        label="Location (City)"
                        name="location"
                        placeholder="e.g. New York"
                        value={formData.location}
                        onChange={handleChange}
                    />
                    <Input
                        label="Password"
                        name="password"
                        type="password"
                        placeholder="Choose a password"
                        value={formData.password}
                        onChange={handleChange}
                        required
                    />

                    <Button type="submit" style={{ width: '100%', marginTop: '1rem' }} disabled={loading}>
                        {loading ? 'Registering...' : 'Register'}
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
