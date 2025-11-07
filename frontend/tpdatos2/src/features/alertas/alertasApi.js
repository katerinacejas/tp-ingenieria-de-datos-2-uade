import { fetchApi } from '../../app/api.js';
import { USE_MOCK } from '../../app/config.js';

const delay = (ms) => new Promise(r => setTimeout(r, ms));

export async function getAlertas() {
  if (USE_MOCK) {
    await delay(200);
    return [
      { id: 'a-1', tipo: 'sensor',     descripcion: 'Temperatura fuera de rango', ciudad: 'CABA',    estado: 'activa'   },
      { id: 'a-2', tipo: 'climatica',  descripcion: 'Humedad extrema',            ciudad: 'Córdoba', estado: 'activa'   },
      { id: 'a-3', tipo: 'sensor',     descripcion: 'Sensor sin señal',           ciudad: 'CABA',    estado: 'resuelta' },
    ];
  }
  // Ajustá el endpoint a tu API real si es distinto
  return await fetchApi('/alertas', { method: 'GET' });
}
