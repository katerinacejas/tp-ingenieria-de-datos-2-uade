import React, { useEffect, useState } from 'react'
import './FacturasList.css'
import Page from '../../components/Page.jsx'
import DataTable from '../../components/DataTable.jsx'
import { getFacturas } from './facturacionApi.js'

export default function FacturasList() {
    const [rows, setRows] = useState([])
    useEffect(() => { (async () => setRows(await getFacturas()))() }, [])

    const handlePagar = (facturaId) => {
        console.log('Pagar factura:', facturaId)
    
    }

    const handleVerFactura = (facturaId) => {
        console.log('Ver factura:', facturaId)
    
    }

    const columns = [
        { key: 'id', header: 'Factura' },
        { key: 'usuario', header: 'Usuario' },
        { key: 'fecha', header: 'Fecha' },
        { key: 'total', header: 'Total', render: v => `$${v}` },
        { key: 'estado', header: 'Estado' },
        { 
            key: 'acciones', 
            header: 'Acciones', 
            render: (_, row) => (
                <div className="hstack">
                    <button 
                        className="btn sm" 
                        onClick={() => handlePagar(row.id)}
                        disabled={row.estado === 'pagada'}
                    >
                        Pagar
                    </button>
                    <button 
                        className="btn ghost sm" 
                        onClick={() => handleVerFactura(row.id)}
                    >
                        Ver Factura
                    </button>
                </div>
            )
        },
    ]

    return (
        <Page title="Facturación">
            <DataTable columns={columns} data={rows} />
        </Page>
    )
}