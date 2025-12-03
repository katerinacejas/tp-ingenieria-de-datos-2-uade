import React from 'react'
import './Stat.css'

export default function Stat({ label, value, hint, color = 'var(--primary)' }) {
    return (
        <div className="card kpi" style={{ borderLeft: `4px solid ${color}` }}>
            <div className="label">{label}</div>
            <div className="value">{value}</div>
            <div className={`hint ${!hint ? 'hint-empty' : ''}`}>{hint || ''}</div>
        </div>
    )
}