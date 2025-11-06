import { fetchApi } from '../../app/api.js';
import { USE_MOCK } from '../../app/config.js';

const delay = (ms) => new Promise(r => setTimeout(r, ms));

let MOCK_SENSORES = [
  { id: 's-001', nombre: 'Temp Norte', ciudad: 'Salta', pais: 'AR', estado: 'activo' },
];

// --- NUEVO: mediciones mock por sensor ---
const makeSerie = (n = 48) => {
  const base = Date.now() - n * 3600_000;
  return Array.from({ length: n }, (_, i) => {
    const t = new Date(base + i * 3600_000);
    // valores pseudoaleatorios en rango 15–30
    const valor = 15 + Math.round((Math.sin(i / 3) + 1) * 7.5 + Math.random() * 2);
    return { fecha: t.toISOString(), valor };
  });
};

let MOCK_MEDICIONES = {
  's-001': makeSerie(72),
};

export async function listarSensores() {
  if (USE_MOCK) { await delay(150); return [...MOCK_SENSORES]; }
  return await fetchApi('/sensores', { method: 'GET' });
}

export async function crearSensor(payload) {
  if (USE_MOCK) {
    await delay(150);
    const nuevo = { id: 's-' + Date.now(), ...payload };
    MOCK_SENSORES = [nuevo, ...MOCK_SENSORES];
    // inicializamos serie vacía para el nuevo sensor
    MOCK_MEDICIONES[nuevo.id] = makeSerie(24);
    return nuevo;
  }
  return await fetchApi('/sensores', { method: 'POST', body: JSON.stringify(payload) });
}

// --- NUEVO: obtener mediciones de un sensor ---
export async function listarMediciones(sensorId, { limit = 100 } = {}) {
  if (USE_MOCK) {
    await delay(150);
    const serie = MOCK_MEDICIONES[sensorId] || [];
    // devolvemos las últimas `limit`
    return serie.slice(-limit);
  }
  return await fetchApi(`/sensores/${sensorId}/mediciones?limit=${limit}`, { method: 'GET' });
}

// alias para no tocar tu import actual en SensorDetalle.jsx
export { listarMediciones as getMediciones };
