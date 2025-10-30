import React from 'react'
import './Dashboard.css'
import Stat from '../../components/Stat.jsx'
import Chart from '../../components/Chart.jsx'

const demo = Array.from({ length: 14 }).map((_, i) => ({ fecha: `Día ${i + 1}`, valor: Math.round(Math.random() * 20 + 10) }))

export default function Dashboard() {
    return (
        <div className="dashboard">
            <div className="grid-kpi">
                <Stat label="Sensores activos" value={128} hint="+5 hoy" />
                <Stat label="Alertas activas" value={9} color="var(--warning)" />
                <Stat label="Procesos pendientes" value={3} color="var(--primary-700)" />
                <Stat label="Facturas vencidas" value={2} color="var(--danger)" />
            </div>
            <h3 className="section-title">Mediciones (últimos 14 días)</h3>
            <Chart data={demo} />
        </div>
    )
}