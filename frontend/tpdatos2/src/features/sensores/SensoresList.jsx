import React, { useEffect, useState } from 'react'
import './SensoresList.css'
import Page from '../../components/Page.jsx'
import DataTable from '../../components/DataTable.jsx'
import { getSensores } from './sensoresApi.js'
import { Link } from 'react-router-dom'

export default function SensoresList() {
    const [rows, setRows] = useState([])
    const [loading, setLoading] = useState(false)

    useEffect(() => { (async () => { setLoading(true); setRows(await getSensores()); setLoading(false) })() }, [])

    const columns = [
        { key: 'id', header: 'ID' },
        { key: 'nombre', header: 'Nombre', render: (v, row) => <Link to={`/sensores/${row.id}`}>{v}</Link> },
        { key: 'ciudad', header: 'Ciudad' },
        { key: 'pais', header: 'País' },
        { key: 'estado', header: 'Estado' },
    ]

    return (
        <Page title="Sensores" actions={<button className="btn">Nuevo sensor</button>}>
            {loading ? <div className="card" style={{ padding: 16 }}>Cargando…</div> : <DataTable columns={columns} data={rows} />}
        </Page>
    )
}