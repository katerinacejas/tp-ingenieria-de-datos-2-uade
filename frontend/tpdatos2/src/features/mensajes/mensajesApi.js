import { USE_MOCK } from '../../app/config.js'
import dayjs from 'dayjs'

// Mocks
const me = { id: 'u-me', nombre: 'Kate' }

const convPriv = [
    { id: 'c-p-1', tipo: 'privado', nombre: 'Soporte', ultimo: 'Revisamos el sensor', ts: Date.now() - 1000 * 60 * 5 },
    { id: 'c-p-2', tipo: 'privado', nombre: 'Administrador', ultimo: 'Listo el alta', ts: Date.now() - 1000 * 60 * 30 },
]
const convGrup = [
    { id: 'c-g-1', tipo: 'grupo', nombre: 'Mantenimiento', ultimo: 'Cambio de batería', ts: Date.now() - 1000 * 60 * 10 },
    { id: 'c-g-2', tipo: 'grupo', nombre: 'Operaciones', ultimo: 'Nuevo proceso anual', ts: Date.now() - 1000 * 60 * 60 },
]

// mensajes por conversación
const storeMsgs = {
    'c-p-1': [
        { id: 'm1', autor: me, texto: 'Hola Soporte', ts: Date.now() - 1000 * 60 * 25 },
        { id: 'm2', autor: { id: 'u-s1', nombre: 'Soporte' }, texto: '¡Hola! Vimos tu caso.', ts: Date.now() - 1000 * 60 * 23 },
        { id: 'm3', autor: me, texto: 'Gracias 🙌', ts: Date.now() - 1000 * 60 * 21 },
    ],
    'c-p-2': [
        { id: 'm1', autor: { id: 'u-a1', nombre: 'Administrador' }, texto: 'Tu usuario está activo.', ts: Date.now() - 1000 * 60 * 40 },
    ],
    'c-g-1': [
        { id: 'm1', autor: { id: 'u-t1', nombre: 'Técnico 1' }, texto: 'Sensor Palermo con batería baja.', ts: Date.now() - 1000 * 60 * 15 },
        { id: 'm2', autor: me, texto: 'Ok, programemos visita.', ts: Date.now() - 1000 * 60 * 12 },
    ],
    'c-g-2': [
        { id: 'm1', autor: { id: 'u-op', nombre: 'Operador' }, texto: 'Arrancamos proceso anual.', ts: Date.now() - 1000 * 60 * 80 },
    ],
}

export async function listConversaciones(tipo = 'privado') {
    if (USE_MOCK) {
        await new Promise(r => setTimeout(r, 200))
        const list = tipo === 'grupo' ? convGrup : convPriv
        // ordenar por ts desc
        return [...list].sort((a, b) => b.ts - a.ts)
    }
    // TODO: GET /mensajes/conversaciones?tipo=
    return []
}

export async function listMensajes(convId) {
    if (USE_MOCK) {
        await new Promise(r => setTimeout(r, 150))
        return storeMsgs[convId] ? [...storeMsgs[convId]] : []
    }
    // TODO: GET /mensajes?convId=
    return []
}

export async function enviarMensaje(convId, texto) {
    if (USE_MOCK) {
        await new Promise(r => setTimeout(r, 120))
        const nuevo = { id: `m-${Date.now()}`, autor: me, texto, ts: Date.now() }
        storeMsgs[convId] = (storeMsgs[convId] || []).concat(nuevo)
        // actualizar “ultimo” en mocks
        const all = [...convPriv, ...convGrup]
        const conv = all.find(c => c.id === convId)
        if (conv) { conv.ultimo = texto; conv.ts = nuevo.ts }
        return nuevo
    }
    // TODO: POST /mensajes { convId, texto }
    return null
}

export const fmt = (ts) => dayjs(ts).format('DD/MM HH:mm')
