import { USE_MOCK } from '../../app/axios.js'

export async function getFacturas() {
    if (USE_MOCK) {
        await new Promise(r => setTimeout(r, 200))
        return [
            { id: 'f-100', usuario: 'kate', fecha: '2025-10-01', total: 3200, estado: 'pendiente' },
            { id: 'f-101', usuario: 'kate', fecha: '2025-09-01', total: 2100, estado: 'pagada' },
            { id: 'f-102', usuario: 'kate', fecha: '2025-08-01', total: 1800, estado: 'vencida' },
        ]
    }
    return []
}