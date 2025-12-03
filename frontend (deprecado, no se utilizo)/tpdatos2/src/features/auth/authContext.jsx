// src/features/auth/authContext.jsx
import React, { createContext, useContext, useMemo, useState } from 'react';
import { jwtDecode } from 'jwt-decode';

const AuthCtx = createContext(null);

function decodeToken(token) {
  if (!token) return { logged: false, role: null, email: null, exp: 0 };
  try {
    const p = jwtDecode(token);
    return {
      logged: true,
      role: p.rol || p.role || (Array.isArray(p.roles) ? p.roles[0] : null),
      email: p.email || p.sub || null,
      exp: (p.exp || 0) * 1000,
    };
  } catch {
    return { logged: false, role: null, email: null, exp: 0 };
  }
}

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem('token'));

  const user = useMemo(() => decodeToken(token), [token]);
  const logged = !!user.logged;

  const login = (tkn) => {
    localStorage.setItem('token', tkn);
    setToken(tkn);
  };

  const logout = () => {
    localStorage.removeItem('token');
    setToken(null);
  };

  const hasRole = (role) => user?.role === role;
  const hasAnyRole = (roles = []) => roles.includes(user?.role);

  return (
    <AuthCtx.Provider value={{ token, user, logged, login, logout, hasRole, hasAnyRole }}>
      {children}
    </AuthCtx.Provider>
  );
}

export const useAuth = () => useContext(AuthCtx);
