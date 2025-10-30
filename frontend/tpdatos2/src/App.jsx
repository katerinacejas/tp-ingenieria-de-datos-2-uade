import React from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import Navbar from './components/Navbar.jsx'
import Sidebar from './components/Sidebar.jsx'
import Dashboard from './features/dashboard/Dashboard.jsx'
import Login from './features/auth/Login.jsx'
import SensoresList from './features/sensores/SensoresList.jsx'
import SensorDetalle from './features/sensores/SensorDetalle.jsx'
import AlertasList from './features/alertas/AlertasList.jsx'
import ProcesosList from './features/procesos/ProcesosList.jsx'
import FacturasList from './features/facturacion/FacturasList.jsx'

export default function App() {
  const logged = true; // para demo; reemplazar por contexto de auth

  if (!logged) {
    return <Login />
  }

  return (
    <div className="app-shell">
      <Sidebar />
      <Navbar />
      <main>
        <Routes>
          <Route index element={<Dashboard />} />
          <Route path="/sensores" element={<SensoresList />} />
          <Route path="/sensores/:id" element={<SensorDetalle />} />
          <Route path="/alertas" element={<AlertasList />} />
          <Route path="/procesos" element={<ProcesosList />} />
          <Route path="/facturacion" element={<FacturasList />} />
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      </main>
    </div>
  )
}