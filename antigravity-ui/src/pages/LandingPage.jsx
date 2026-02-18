import React from 'react';
import { Link } from 'react-router-dom';
import { Heart, Users, Shield, MessageCircle, ArrowRight } from 'lucide-react';
import './LandingPage.css';

const LandingPage = () => {
    return (
        <div className="landing-container">
            {/* Public Header */}
            <header className="landing-header glass-panel">
                <div className="landing-brand">Mitra</div>
                <nav className="landing-nav">
                    <Link to="/login" className="nav-link">Login</Link>
                    <Link to="/register" className="nav-link btn-register-link">Get Started</Link>
                </nav>
            </header>

            {/* Hero Section */}
            <section className="hero-section">
                <div className="hero-content">
                    <h1 className="hero-title">
                        <span className="gradient-text">Mitra</span> <br />
                        Where Your Story Matters.
                    </h1>
                    <p className="hero-subtitle">
                        A Supportive Community for the Experience Generation.
                        Connect with friends, stay updated with local news, and share your wisdom with the world.
                    </p>
                    <div className="hero-actions">
                        <Link to="/register" className="cta-btn primary-cta">
                            Join Our Community <ArrowRight size={20} />
                        </Link>
                        <Link to="/login" className="cta-btn secondary-cta">
                            Welcome Back
                        </Link>
                    </div>
                </div>
                <div className="hero-visual">
                    <div className="glass-card main-card">
                        <div className="card-header">
                            <div className="user-icon"><Users size={24} /></div>
                            <span>Join 5,000+ Seniors</span>
                        </div>
                        <p>"I found my old school friends here and we now talk every day!"</p>
                    </div>
                    <div className="floating-elements">
                        <div className="float-panel p1 glass-panel"><Heart size={20} /> Supportive</div>
                        <div className="float-panel p2 glass-panel"><Shield size={20} /> Secure</div>
                        <div className="float-panel p3 glass-panel"><MessageCircle size={20} /> Heard</div>
                    </div>
                </div>
            </section>

            {/* Features Section */}
            <section className="features-section">
                <h2 className="section-title">Designed for <span className="gradient-text">Connection</span></h2>
                <div className="features-grid">
                    <div className="feature-card glass-panel">
                        <div className="feature-icon"><MessageCircle size={32} /></div>
                        <h3>Stories</h3>
                        <p>Share your life experiences with your local community, district, or the entire nation.</p>
                    </div>
                    <div className="feature-card glass-panel">
                        <div className="feature-icon"><Users size={32} /></div>
                        <h3>Groups</h3>
                        <p>Connect with people who share your interests, hobbies, and history.</p>
                    </div>
                    <div className="feature-card glass-panel">
                        <div className="feature-icon"><ArrowRight size={32} /></div>
                        <h3>Local Updates</h3>
                        <p>Stay informed about what's happening right in your neighborhood and taluk.</p>
                    </div>
                </div>
            </section>

            {/* Footer */}
            <footer className="landing-footer">
                <div className="footer-content">
                    <div className="footer-brand">Mitra</div>
                    <p>© 2026 Mitra Community. Built with love for you.</p>
                </div>
            </footer>
        </div>
    );
};

export default LandingPage;
