import React, { useEffect, useState } from 'react'
import './AlertasList.css'
import Page from '../../components/Page.jsx'
import DataTable from '../../components/DataTable.jsx'
import { getAlertas } from './alertasApi.js'

export default function AlertasList() {
    const [rows, setRows] = useState([])
    useEffect(() => { (async () => setRows(await getAlertas()))() }, [])

    const handleVerAlerta = (alertaId) => {
        console.log('Ver alerta:', alertaId)

    }

    const columns = [
        { key: 'id', header: 'ID' },
        { key: 'tipo', header: 'Tipo' },
        { key: 'descripcion', header: 'Descripción' },
        { key: 'ciudad', header: 'Ciudad' },
        { key: 'estado', header: 'Estado' },
        { 
            key: 'acciones', 
            header: 'Acciones', 
            render: (_, row) => (
                <button 
                    className="btn ghost sm" 
                    onClick={() => handleVerAlerta(row.id)}
                >
                    Ver alerta
                </button>
            )
        },
    ]

    return (
        <Page title="Alertas">
            <DataTable columns={columns} data={rows} />
        </Page>
    )
}