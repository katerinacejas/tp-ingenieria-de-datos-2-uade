import React, { useEffect, useState } from 'react'
import './ProcesosList.css'
import Page from '../../components/Page.jsx'
import DataTable from '../../components/DataTable.jsx'
import { getProcesos } from './procesosApi.js'

export default function ProcesosList() {
    const [rows, setRows] = useState([])
    useEffect(() => { (async () => setRows(await getProcesos()))() }, [])

    const columns = [
        { key: 'id', header: 'ID' },
        { key: 'nombre', header: 'Proceso' },
        { key: 'costo', header: 'Costo', render: v => `$${v}` },
        { key: 'estado', header: 'Estado' },
    ]

    return (
        <Page title="Procesos" actions={<button className="btn">Nueva solicitud</button>}>
            <DataTable columns={columns} data={rows} />
        </Page>
    )
}