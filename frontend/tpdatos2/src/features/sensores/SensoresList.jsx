import React, { useEffect, useState } from 'react'
import './SensoresList.css'
import Page from '../../components/Page.jsx'
import DataTable from '../../components/DataTable.jsx'
import { listarSensores as getSensores } from './sensoresApi.js'
import { Link } from 'react-router-dom'

export default function SensoresList() {
    const [rows, setRows] = useState([])
    const [loading, setLoading] = useState(false)

    useEffect(() => { (async () => { setLoading(true); setRows(await getSensores()); setLoading(false) })() }, [])

    const handleAgregarDatos = (sensorId) => {
        console.log('Agregar datos del sensor:', sensorId)

    }

    const handleVerAlertas = (sensorId) => {
        console.log('Ver alertas del sensor:', sensorId)

    }

    const columns = [
        { key: 'id', header: 'ID' },
        { key: 'nombre', header: 'Nombre', render: (v, row) => <Link to={`/sensores/${row.id}`}>{v}</Link> },
        { key: 'ciudad', header: 'Ciudad' },
        { key: 'pais', header: 'País' },
        { key: 'estado', header: 'Estado' },
        { 
            key: 'acciones', 
            header: 'Acciones', 
            render: (_, row) => (
                <div className="hstack">
                    <button 
                        className="btn sm" 
                        onClick={() => handleAgregarDatos(row.id)}
                    >
                        Agregar datos
                    </button>
                    <button 
                        className="btn ghost sm" 
                        onClick={() => handleVerAlertas(row.id)}
                    >
                        Alertas
                    </button>
                </div>
            )
        },
    ]

    return (
        <Page title="Sensores" actions={<button className="btn">Nuevo sensor</button>}>
            {loading ? <div className="card" style={{ padding: 16 }}>Cargando…</div> : <DataTable columns={columns} data={rows} />}
        </Page>
    )
}