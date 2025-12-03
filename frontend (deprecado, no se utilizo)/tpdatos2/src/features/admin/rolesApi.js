
import { USE_MOCK, API_URL } from '../../app/config.js';

async function authFetch(path, opts = {}) {
  const token = localStorage.getItem('token');
  const res = await fetch(API_URL + path, {
    headers: { 'Content-Type': 'application/json', ...(token ? { Authorization: `Bearer ${token}` } : {}) },
    ...opts,
  });

  // Manejo básico
  if (res.status === 204) return null;

  let data = null;
  try { data = await res.json(); } catch { /* sin body */ }

  if (!res.ok) {
    const msg = data?.message || data?.error || `HTTP ${res.status}`;
    const err = new Error(msg);
    err.status = res.status;
    err.data = data;
    throw err;
  }
  return data;
}

/* ===========================
   MOCK STORE (para demo)
=========================== */
let TECNICOS = [
  { id: 't-001', nombre: 'Lucía Torres', email: 'lucia@empresa.com', createdAt: Date.now() - 86_400_000 },
];
let SOLICITUDES = [
  { id: 'sol-101', nombre: 'Juan Pérez',  email: 'juan@mail.com',  createdAt: Date.now() - 3_600_000 },
  { id: 'sol-102', nombre: 'María López', email: 'maria@mail.com', createdAt: Date.now() - 7_200_000 },
];

const delay = (ms) => new Promise(r => setTimeout(r, ms));

/* ===========================
   FUNCIONES PÚBLICAS
=========================== */

export async function listarTecnicos() {
  if (USE_MOCK) {
    await delay(150);
    // devolver copia
    return [...TECNICOS].sort((a,b)=>b.createdAt-a.createdAt);
  }
  // Ajustá el endpoint a tu back real:
  // GET /admin/roles/tecnicos
  return await authFetch('/admin/roles/tecnicos', { method: 'GET' });
}

export async function listarSolicitudesTecnico() {
  if (USE_MOCK) {
    await delay(150);
    return [...SOLICITUDES].sort((a,b)=>b.createdAt-a.createdAt);
  }
  // GET /admin/roles/solicitudes-tecnico
  return await authFetch('/admin/roles/solicitudes-tecnico', { method: 'GET' });
}

export async function crearTecnico({ nombre, email, password }) {
  if (USE_MOCK) {
    await delay(200);
    const nuevo = { id: 't-' + Date.now(), nombre, email, createdAt: Date.now() };
    TECNICOS = [nuevo, ...TECNICOS];
    return nuevo;
  }
  // POST /admin/roles/tecnicos
  return await authFetch('/admin/roles/tecnicos', {
    method: 'POST',
    body: { nombre, email, password },
  });
}

export async function aprobarSolicitud(id) {
  if (USE_MOCK) {
    await delay(200);
    const sol = SOLICITUDES.find(s => s.id === id);
    if (sol) {
      // al aprobar, se crea el técnico
      const t = { id: 't-' + Date.now(), nombre: sol.nombre, email: sol.email, createdAt: Date.now() };
      TECNICOS = [t, ...TECNICOS];
      SOLICITUDES = SOLICITUDES.filter(s => s.id !== id);
      return true;
    }
    return false;
  }
  // POST /admin/roles/solicitudes-tecnico/:id/aprobar
  await authFetch(`/admin/roles/solicitudes-tecnico/${id}/aprobar`, { method: 'POST' });
  return true;
}

export async function rechazarSolicitud(id) {
  if (USE_MOCK) {
    await delay(150);
    SOLICITUDES = SOLICITUDES.filter(s => s.id !== id);
    return true;
  }
  // POST /admin/roles/solicitudes-tecnico/:id/rechazar
  await authFetch(`/admin/roles/solicitudes-tecnico/${id}/rechazar`, { method: 'POST' });
  return true;
}
