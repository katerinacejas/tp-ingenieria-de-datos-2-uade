import React, { useEffect, useState } from 'react'
import './SensorDetalle.css'
import Page from '../../components/Page.jsx'
import Chart from '../../components/Chart.jsx'
import { getMediciones } from './sensoresApi.js'
import { useParams } from 'react-router-dom'

export default function SensorDetalle() {
    const { id } = useParams()
    const [data, setData] = useState([])

    useEffect(() => { (async () => setData(await getMediciones(id)))() }, [id])

    return (
        <Page title={`Sensor ${id}`}>
            <div className="tabs">
                <div className="tab active">Mediciones</div>
                <div className="tab">Alertas</div>
            </div>
            <Chart data={data} dataKey="valor" xKey="fecha" />
        </Page>
    )
}