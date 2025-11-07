import { fetchApi } from '../../app/api.js'
import { USE_MOCK } from '../../app/config.js'

const delay = (ms) => new Promise(r => setTimeout(r, ms))

export async function getProcesos() {
  if (USE_MOCK) {
    await delay(150)
    return [
      { id: 'p-1', nombre: 'Promedios por ciudad (mensual)', costo: 1200, estado: 'operativo' },
      { id: 'p-2', nombre: 'Detección de outliers',          costo:  950, estado: 'operativo' },
      { id: 'p-3', nombre: 'Reporte anual por país',         costo: 5000, estado: 'mantenimiento' },
    ]
  }
  // endpoint real (ajustá si tu backend usa otro path)
  return await fetchApi('/procesos', { method: 'GET' })
}
