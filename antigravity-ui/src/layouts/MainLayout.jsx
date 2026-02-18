import React from 'react';
import { NavLink } from 'react-router-dom';
import { LayoutGrid, Users, Newspaper, Settings, LogOut } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import './MainLayout.css';

const MainLayout = ({ children }) => {
    const { logout, user } = useAuth();

    return (
        <div className="main-layout">
            {/* Left Sidebar: Navigation */}
            <nav className="left-sidebar glass-panel">
                <div className="brand">
                    <h2 className="brand-name">Mitra</h2>
                </div>

                <div className="nav-links">
                    <NavLink to="/feed" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
                        <LayoutGrid size={24} />
                        <span>Stories</span>
                    </NavLink>
                    <NavLink to="/group" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
                        <Users size={24} />
                        <span>Group</span>
                    </NavLink>
                    <NavLink to="/news" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
                        <Newspaper size={24} />
                        <span>News</span>
                    </NavLink>
                    <NavLink to="/settings" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
                        <Settings size={24} />
                        <span>Settings</span>
                    </NavLink>
                </div>

                <div className="user-section">
                    <div className="user-info">
                        <div className="avatar">{user?.username?.[0]?.toUpperCase()}</div>
                        <span className="username">{user?.username}</span>
                    </div>
                    <button onClick={logout} className="logout-btn">
                        <LogOut size={20} />
                        <span>Logout</span>
                    </button>
                </div>
            </nav>

            {/* Center Content: Feed */}
            <main className="center-content">
                {children}
            </main>

            {/* Right Sidebar: Ads */}
            <aside className="right-sidebar glass-panel">
                <div className="ads-container">
                    <h4 className="section-title">Sponsored</h4>
                    <div className="ad-card">
                        <div className="ad-image placeholder">Ad Banner #1</div>
                        <p className="ad-text">Discover new ways to stay connected with your family.</p>
                    </div>
                    <div className="ad-card">
                        <div className="ad-image placeholder">Ad Banner #2</div>
                        <p className="ad-text">Senior Health Expo - Join us this weekend!</p>
                    </div>
                </div>
            </aside>
        </div>
    );
};

export default MainLayout;
