import React, { useEffect, useState } from 'react'
import './FacturasList.css'
import Page from '../../components/Page.jsx'
import DataTable from '../../components/DataTable.jsx'
import { getFacturas } from './facturacionApi.js'

export default function FacturasList() {
    const [rows, setRows] = useState([])
    useEffect(() => { (async () => setRows(await getFacturas()))() }, [])

    const columns = [
        { key: 'id', header: 'Factura' },
        { key: 'usuario', header: 'Usuario' },
        { key: 'fecha', header: 'Fecha' },
        { key: 'total', header: 'Total', render: v => `$${v}` },
        { key: 'estado', header: 'Estado' },
    ]

    return (
        <Page title="Facturación">
            <DataTable columns={columns} data={rows} />
        </Page>
    )
}