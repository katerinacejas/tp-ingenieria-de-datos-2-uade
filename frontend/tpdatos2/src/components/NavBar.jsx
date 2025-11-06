import React from 'react'
import './Navbar.css'
import { FaBell, FaUserCircle } from 'react-icons/fa'

export default function Navbar() {
    return (
        <header className="navbar card">
            <div className="brand">TP Persistencia Políglota</div>
            <div className="actions">
                <button className="icon-btn" title="Notificaciones"><FaBell /></button>
                <div className="user"><FaUserCircle /> Kate</div>
            </div>
        </header>
    )
}