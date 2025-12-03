import React from 'react'
import './Chart.css'
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts'

export default function Chart({ data, dataKey = "valor", xKey = "fecha" }) {
    return (
        <div className="card chart">
            <ResponsiveContainer width="100%" height={280}>
                <LineChart data={data}>
                    <CartesianGrid stroke="#15202b" />
                    <XAxis dataKey={xKey} tick={{ fill: '#9fb0c0' }} />
                    <YAxis tick={{ fill: '#9fb0c0' }} />
                    <Tooltip contentStyle={{ background: '#0b1016', border: '1px solid #1f2a37', color: '#e6edf3' }} />
                    <Line type="monotone" dataKey={dataKey} stroke="#3b82f6" strokeWidth={2} dot={false} />
                </LineChart>
            </ResponsiveContainer>
        </div>
    )
}