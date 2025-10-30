import { USE_MOCK } from '../../app/axios.js'
import dayjs from 'dayjs'

export async function getSensores() {
    if (USE_MOCK) {
        await new Promise(r => setTimeout(r, 300))
        return [
            { id: 's-100', nombre: 'Sensor Palermo', ciudad: 'CABA', pais: 'AR', estado: 'activo' },
            { id: 's-200', nombre: 'Sensor Recoleta', ciudad: 'CABA', pais: 'AR', estado: 'activo' },
            { id: 's-300', nombre: 'Sensor Córdoba', ciudad: 'Córdoba', pais: 'AR', estado: 'inactivo' }
        ]
    }
    // fetch real aquí
    return []
}

export async function getMediciones(sensorId) {
    if (USE_MOCK) {
        await new Promise(r => setTimeout(r, 200))
        return Array.from({ length: 24 }).map((_, i) => ({ fecha: dayjs().subtract(23 - i, 'hour').format('HH:mm'), valor: Math.round(Math.random() * 8) + 18 }))
    }
    return []
}