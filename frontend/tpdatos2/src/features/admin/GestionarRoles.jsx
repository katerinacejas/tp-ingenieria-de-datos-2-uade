import React, { useEffect, useState } from 'react'
import './GestionarRoles.css'
import Page from '../../components/Page.jsx'
import DataTable from '../../components/DataTable.jsx'
import { useAuth } from '../auth/authContext.jsx'
import {
  listarSolicitudesTecnico,
  aprobarSolicitud,
  rechazarSolicitud,
  crearTecnico,
  listarTecnicos
} from './rolesApi.js'

export default function GestionarRoles(){
  const { hasRole } = useAuth()

  const [solicitudes, setSolicitudes] = useState([])
  const [tecnicos, setTecnicos] = useState([])
  const [loading, setLoading] = useState(true)
  const [form, setForm] = useState({ nombre:'', email:'', password:'', password2:'' })
  const [msg, setMsg] = useState(null)

  const load = async () => {
    setLoading(true)
    const [sol, tech] = await Promise.all([
      listarSolicitudesTecnico(),
      listarTecnicos()
    ])
    setSolicitudes(sol)
    setTecnicos(tech)
    setLoading(false)
  }

  useEffect(()=>{ load() },[])

  const aprobar = async (id) => {
    await aprobarSolicitud(id)
    setMsg('Solicitud aprobada y técnico creado.')
    load()
  }
  const rechazar = async (id) => {
    await rechazarSolicitud(id)
    setMsg('Solicitud rechazada.')
    load()
  }

  const submit = async (e) => {
    e.preventDefault()
    setMsg(null)
    if(form.password !== form.password2){
      setMsg('Las contraseñas no coinciden.')
      return
    }
    await crearTecnico({ nombre: form.nombre.trim(), email: form.email.trim(), password: form.password })
    setForm({ nombre:'', email:'', password:'', password2:'' })
    setMsg('Técnico creado correctamente.')
    load()
  }

  const colsSolicitudes = [
    { key:'nombre', header:'Nombre' },
    { key:'email', header:'Email' },
    { key:'createdAt', header:'Fecha', render:v=> new Date(v).toLocaleString() },
    { key:'acciones', header:'Acciones', render:(_,row)=>(
      <div className="hstack">
        <button className="btn sm" onClick={()=>aprobar(row.id)}>Aprobar</button>
        <button className="btn ghost sm" onClick={()=>rechazar(row.id)}>Rechazar</button>
      </div>
    ) },
  ]

  const colsTecnicos = [
    { key:'nombre', header:'Nombre' },
    { key:'email', header:'Email' },
    { key:'createdAt', header:'Alta', render:v=> new Date(v).toLocaleDateString() },
  ]

  return (
    <Page title="Gestión de roles" actions={null}>
      <div className="grid-roles">
        {/* Alta manual de técnico */}
        <section className="card panel">
          <h3>Alta de usuario de mantenimiento</h3>
          <p className="muted">Crear un técnico directamente (rol = técnico).</p>

          <form className="grid-form" onSubmit={submit}>
            <div className="form-group">
              <label className="label" htmlFor="nombre">Nombre completo</label>
              <input id="nombre" className="input"
                     value={form.nombre}
                     onChange={e=>setForm(f=>({...f,nombre:e.target.value}))}
                     placeholder="Nombre y apellido" required />
            </div>

            <div className="form-group">
              <label className="label" htmlFor="email">Email</label>
              <input id="email" className="input" type="email"
                     value={form.email}
                     onChange={e=>setForm(f=>({...f,email:e.target.value}))}
                     placeholder="tecnico@mail.com" required />
            </div>

            <div className="form-row2">
              <div className="form-group">
                <label className="label" htmlFor="pass">Contraseña</label>
                <input id="pass" className="input" type="password"
                       value={form.password}
                       onChange={e=>setForm(f=>({...f,password:e.target.value}))}
                       placeholder="••••••••" required />
              </div>
              <div className="form-group">
                <label className="label" htmlFor="pass2">Repetir</label>
                <input id="pass2" className="input" type="password"
                       value={form.password2}
                       onChange={e=>setForm(f=>({...f,password2:e.target.value}))}
                       placeholder="••••••••" required />
              </div>
            </div>

            <div className="form-actions">
              <button className="btn" type="submit">Crear técnico</button>
            </div>
            {msg && <div className="info">{msg}</div>}
          </form>
        </section>

        {/* Solicitudes pendientes */}
        <section className="card panel">
          <h3>Solicitudes para rol Técnico</h3>
          {loading
            ? <div className="muted">Cargando…</div>
            : <DataTable columns={colsSolicitudes} data={solicitudes} />
          }
        </section>

        {/* Listado de técnicos */}
        <section className="card panel">
          <h3>Técnicos activos</h3>
          {loading
            ? <div className="muted">Cargando…</div>
            : <DataTable columns={colsTecnicos} data={tecnicos} />
          }
        </section>
      </div>
    </Page>
  )
}
