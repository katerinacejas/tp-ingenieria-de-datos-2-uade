import { USE_MOCK } from '../../app/axios.js'

export async function getProcesos() {
    if (USE_MOCK) {
        await new Promise(r => setTimeout(r, 150))
        return [
            { id: 'p-1', nombre: 'Promedios por ciudad (mensual)', costo: 1200, estado: 'operativo' },
            { id: 'p-2', nombre: 'Detección de outliers', costo: 950, estado: 'operativo' },
            { id: 'p-3', nombre: 'Reporte anual por país', costo: 5000, estado: 'mantenimiento' }
        ]
    }
    return []
}