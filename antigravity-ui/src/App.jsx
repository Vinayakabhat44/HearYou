import Settings from './pages/Settings'
import './index.css'
import './apm'
import { ApmRoutes } from '@elastic/apm-rum-react'
import { BrowserRouter as Router, Route, Navigate } from 'react-router-dom'
import { AuthProvider, useAuth } from './context/AuthContext'
import LandingPage from './pages/LandingPage'
import Login from './pages/Login'
import Register from './pages/Register'
import MainLayout from './layouts/MainLayout'
import Stories from './pages/Stories'
import Group from './pages/Group'
import News from './pages/News'

const ProtectedRoute = ({ children }) => {
  const { user } = useAuth();
  if (!user) return <Navigate to="/login" replace />;
  return <MainLayout>{children}</MainLayout>;
};

const PublicRoute = ({ children }) => {
  const { user } = useAuth();
  if (user) return <Navigate to="/feed" replace />;
  return children;
};

function App() {
  return (
    <Router>
      <AuthProvider>
        <div className="app-container">
          <ApmRoutes>
            <Route path="/" element={
              <PublicRoute>
                <LandingPage />
              </PublicRoute>
            } />

            <Route path="/login" element={
              <PublicRoute>
                <Login />
              </PublicRoute>
            } />
            <Route path="/register" element={
              <PublicRoute>
                <Register />
              </PublicRoute>
            } />

            <Route path="/feed" element={
              <ProtectedRoute>
                <Stories />
              </ProtectedRoute>
            } />
            <Route path="/group" element={
              <ProtectedRoute>
                <Group />
              </ProtectedRoute>
            } />
            <Route path="/news" element={
              <ProtectedRoute>
                <News />
              </ProtectedRoute>
            } />
            <Route path="/settings" element={
              <ProtectedRoute>
                <Settings />
              </ProtectedRoute>
            } />

            <Route path="*" element={<Navigate to="/" replace />} />
          </ApmRoutes>
        </div>
      </AuthProvider>
    </Router>
  )
}

export default App
