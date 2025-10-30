import { USE_MOCK } from '../../app/axios.js'

export async function getAlertas() {
    if (USE_MOCK) {
        await new Promise(r => setTimeout(r, 200))
        return [
            { id: 'a-1', tipo: 'sensor', descripcion: 'Temperatura fuera de rango', ciudad: 'CABA', estado: 'activa' },
            { id: 'a-2', tipo: 'climatica', descripcion: 'Humedad extrema', ciudad: 'Córdoba', estado: 'activa' },
            { id: 'a-3', tipo: 'sensor', descripcion: 'Sensor sin señal', ciudad: 'CABA', estado: 'resuelta' },
        ]
    }
    return []
}