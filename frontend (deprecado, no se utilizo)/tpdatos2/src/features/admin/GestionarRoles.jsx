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
  const [msgType, setMsgType] = useState('success') 

  const load = async () => {
    setLoading(true)
    try {
      const [sol, tech] = await Promise.all([
        listarSolicitudesTecnico(),
        listarTecnicos()
      ])
      setSolicitudes(sol)
      setTecnicos(tech)
    } catch (error) {
      setMsg('Error al cargar los datos.')
      setMsgType('error')
    } finally {
      setLoading(false)
    }
  }

  useEffect(()=>{ load() },[])

  // Auto-ocultar mensajes después de 5 segundos
  useEffect(() => {
    if (msg) {
      const timer = setTimeout(() => {
        setMsg(null)
      }, 5000)
      return () => clearTimeout(timer)
    }
  }, [msg])

  const aprobar = async (id) => {
    try {
      await aprobarSolicitud(id)
      setMsg('Solicitud aprobada y técnico creado correctamente.')
      setMsgType('success')
      load()
    } catch (error) {
      setMsg('Error al aprobar la solicitud.')
      setMsgType('error')
    }
  }

  const rechazar = async (id) => {
    if (!window.confirm('¿Estás seguro de que deseas rechazar esta solicitud?')) {
      return
    }
    try {
      await rechazarSolicitud(id)
      setMsg('Solicitud rechazada correctamente.')
      setMsgType('success')
      load()
    } catch (error) {
      setMsg('Error al rechazar la solicitud.')
      setMsgType('error')
    }
  }

  const submit = async (e) => {
    e.preventDefault()
    setMsg(null)
    
    if(form.password !== form.password2){
      setMsg('Las contraseñas no coinciden.')
      setMsgType('error')
      return
    }

    if(form.password.length < 6){
      setMsg('La contraseña debe tener al menos 6 caracteres.')
      setMsgType('error')
      return
    }

    try {
      await crearTecnico({ nombre: form.nombre.trim(), email: form.email.trim(), password: form.password })
      setForm({ nombre:'', email:'', password:'', password2:'' })
      setMsg('Técnico creado correctamente.')
      setMsgType('success')
      load()
    } catch (error) {
      setMsg('Error al crear el técnico. Verifica los datos.')
      setMsgType('error')
    }
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
          <p className="panel-description">Crear un técnico directamente (rol = técnico).</p>

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
                       placeholder="••••••••" required minLength={6} />
              </div>
              <div className="form-group">
                <label className="label" htmlFor="pass2">Repetir contraseña</label>
                <input id="pass2" className="input" type="password"
                       value={form.password2}
                       onChange={e=>setForm(f=>({...f,password2:e.target.value}))}
                       placeholder="••••••••" required minLength={6} />
              </div>
            </div>

            <div className="form-actions">
              <button className="btn" type="submit">Crear técnico</button>
            </div>
            {msg && <div className={`info info-${msgType}`}>{msg}</div>}
          </form>
        </section>

        {/* Solicitudes pendientes */}
        <section className="card panel">
          <h3>Solicitudes para rol Técnico</h3>
          {loading
            ? <div className="loading-state">Cargando…</div>
            : solicitudes.length === 0 
              ? <div className="empty-state">No hay solicitudes pendientes</div>
              : <DataTable columns={colsSolicitudes} data={solicitudes} />
          }
        </section>

        {/* Listado de técnicos */}
        <section className="card panel">
          <h3>Técnicos activos</h3>
          {loading
            ? <div className="loading-state">Cargando…</div>
            : tecnicos.length === 0
              ? <div className="empty-state">No hay técnicos registrados</div>
              : <DataTable columns={colsTecnicos} data={tecnicos} />
          }
        </section>
      </div>
    </Page>
  )
}
