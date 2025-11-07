// src/components/ProtectedRoute.jsx
import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../features/auth/authContext.jsx';

export default function ProtectedRoute({ allowedRoles, children }) {
  const { logged, user } = useAuth();
  const loc = useLocation();

  if (!logged) return <Navigate to="/login" replace state={{ from: loc }} />;

  if (allowedRoles && !allowedRoles.includes(user.role)) {
    return <Navigate to="/" replace />;
  }
  return children;
}
    