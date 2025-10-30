import React, { useState } from 'react'
import './Login.css'

export default function Login() {
    const [email, setEmail] = useState('')
    const [pass, setPass] = useState('')
    const [showPass, setShowPass] = useState(false)

    const submit = (e) => {
        e.preventDefault()
        alert('Demo login. Integra tu backend cuando quieras.')
    }

    return (
        <div className="login-wrap">
            <div className="login-card card">
                <div className="login-head">
                    <div className="login-brand-circle">TP</div>
                    <div className="login-title">
                        <h1>Iniciar sesión</h1>
                        <p className="muted">Accedé al panel de control</p>
                    </div>
                </div>

                <form onSubmit={submit} className="login-form">
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
                                autoComplete="current-password"
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

                    <div className="form-actions">
                        <label className="remember">
                            <input type="checkbox" /> Recordarme
                        </label>
                        <a className="link" href="#" onClick={(e) => e.preventDefault()}>¿Olvidaste tu contraseña?</a>
                    </div>

                    <button className="btn btn-primary btn-full" type="submit">Entrar</button>
                </form>

                <p className="login-foot muted">
                    ¿No tenés cuenta? <a className="link" href="#" onClick={(e) => e.preventDefault()}>Contactá al admin</a>
                </p>
            </div>
        </div>
    )
}
