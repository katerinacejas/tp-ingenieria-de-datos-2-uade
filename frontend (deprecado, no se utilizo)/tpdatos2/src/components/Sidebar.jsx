import React from 'react'
import './Sidebar.css'
import { NavLink } from 'react-router-dom'
import { FaTachometerAlt, FaComments, FaMicrochip, FaChartLine, FaExclamationTriangle, FaCogs, FaFileInvoiceDollar } from 'react-icons/fa'

export default function Sidebar() {
    return (
        <aside className="sidebar card">
            <nav>
                <NavLink to="/" end> <FaTachometerAlt /> Dashboard</NavLink>
                <NavLink to="/sensores"> <FaMicrochip /> Sensores</NavLink>
                <NavLink to="/alertas"> <FaExclamationTriangle /> Alertas</NavLink>
                <NavLink to="/procesos"> <FaCogs /> Procesos</NavLink>
                <NavLink to="/facturacion"> <FaFileInvoiceDollar /> Facturación</NavLink>
                <NavLink to="/mensajes"> <FaComments/> Mensajes</NavLink>
            </nav>
        </aside>
    )
}