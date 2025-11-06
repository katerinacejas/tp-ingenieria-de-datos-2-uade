import React from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import Dashboard from './features/dashboard/Dashboard.jsx'
import Login from './features/auth/Login.jsx'
import SensoresList from './features/sensores/SensoresList.jsx'
import SensorDetalle from './features/sensores/SensorDetalle.jsx'
import AlertasList from './features/alertas/AlertasList.jsx'
import ProcesosList from './features/procesos/ProcesosList.jsx'
import FacturasList from './features/facturacion/FacturasList.jsx'
import Mensajes from './features/mensajes/Mensajes.jsx'
import Register from './features/auth/Register.jsx'
import ProtectedRoute from './components/ProtectedRoute.jsx'
import GestionarRoles from './features/admin/GestionarRoles.jsx'
import { AuthProvider } from './features/auth/authContext.jsx'

export default function App() {
  return (
    <AuthProvider>
      <Routes>
        {/* públicas */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* privadas */}
        <Route path="/" element={
          <ProtectedRoute allowedRoles={['USUARIO','MANTENIMIENTO','ADMIN']}>
            <Dashboard />
          </ProtectedRoute>
        } />

        <Route path="/facturacion" element={
          <ProtectedRoute allowedRoles={['USUARIO']}>
            <FacturasList />
          </ProtectedRoute>
        } />

        <Route path="/sensores" element={
          <ProtectedRoute allowedRoles={['USUARIO','MANTENIMIENTO']}>
            <SensoresList />
          </ProtectedRoute>
        } />
        <Route path="/sensores/:id" element={
          <ProtectedRoute allowedRoles={['USUARIO','MANTENIMIENTO']}>
            <SensorDetalle />
          </ProtectedRoute>
        } />

        <Route path="/alertas" element={
          <ProtectedRoute allowedRoles={['USUARIO','MANTENIMIENTO']}>
            <AlertasList />
          </ProtectedRoute>
        } />

        <Route path="/procesos" element={
          <ProtectedRoute allowedRoles={['USUARIO','MANTENIMIENTO']}>
            <ProcesosList />
          </ProtectedRoute>
        } />

        <Route path="/mensajes" element={
          <ProtectedRoute allowedRoles={['USUARIO','MANTENIMIENTO']}>
            <Mensajes />
          </ProtectedRoute>
        } />

        <Route path="/gestionarRoles" element={
          <ProtectedRoute allowedRoles={['ADMIN']}>
            <GestionarRoles />
          </ProtectedRoute>
        } />

        {/* fallback */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </AuthProvider>
  )
}
