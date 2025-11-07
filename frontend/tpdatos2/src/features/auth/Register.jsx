import React, { useState } from 'react'
import './Register.css'
import { Link } from 'react-router-dom'

export default function Register() {
  const [nombre, setNombre] = useState('')
  const [email, setEmail] = useState('')
  const [pass, setPass] = useState('')
  const [pass2, setPass2] = useState('')
  const [showPass, setShowPass] = useState(false)
  const [error, setError] = useState('')

  const submit = (e) => {
    e.preventDefault()
    setError('')
    if (pass !== pass2) {
      setError('Las contraseñas no coinciden.')
      return
    }
    // Demo: reemplazar por POST a tu backend
    alert(`Usuario registrado:\n${nombre} - ${email}`)
  }

  return (
    <div className="register-wrap">
      <div className="register-card card">
        <div className="register-head">
          <div className="register-brand-circle">TP</div>
          <div className="register-title">
            <h1>Crear cuenta</h1>
            <p className="muted">Registrate para usar el panel</p>
          </div>
        </div>

        <form onSubmit={submit} className="register-form">
          <div className="form-group">
            <label className="label" htmlFor="nombre">Nombre completo</label>
            <input
              id="nombre"
              className="input"
              value={nombre}
              onChange={(e) => setNombre(e.target.value)}
              placeholder="Tu nombre y apellido"
              required
            />
          </div>

          <div className="form-group">
            <label className="label" htmlFor="email">Email</label>
            <input
              id="email"
              className="input"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="tu@mail.com"
              autoComplete="email"
              required
            />
          </div>

          <div className="form-group">
            <label className="label" htmlFor="password">Contraseña</label>
            <div className="input-ctrl">
              <input
                id="password"
                className="input"
                type={showPass ? 'text' : 'password'}
                value={pass}
                onChange={(e) => setPass(e.target.value)}
                placeholder="••••••••"
                autoComplete="new-password"
                required
              />
              <button
                type="button"
                className="eye-btn"
                aria-label={showPass ? 'Ocultar contraseña' : 'Mostrar contraseña'}
                onClick={() => setShowPass(s => !s)}
              >
                {showPass ? 'Ocultar' : 'Mostrar'}
              </button>
            </div>
          </div>

          <div className="form-group">
            <label className="label" htmlFor="password2">Repetir contraseña</label>
            <input
              id="password2"
              className="input"
              type={showPass ? 'text' : 'password'}
              value={pass2}
              onChange={(e) => setPass2(e.target.value)}
              placeholder="••••••••"
              autoComplete="new-password"
              required
            />
          </div>

          {error && <div className="form-error">{error}</div>}

          <button className="btn btn-primary btn-full" type="submit">Crear cuenta</button>
        </form>

        <p className="register-foot muted">
           ¿Ya tenés cuenta? <Link className="link" to="/login">Iniciar sesión</Link>
        </p>
      </div>
    </div>
  )
}
