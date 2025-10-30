import React, { useState } from 'react'
import './Login.css'

export default function Login() {
    const [email, setEmail] = useState('')
    const [pass, setPass] = useState('')
    const submit = e => { e.preventDefault(); alert('Demo login. Integra tu backend cuando quieras.') }

    return (
        <div className="login-wrap">
            <form className="card login" onSubmit={submit}>
                <h2>Iniciar sesión</h2>
                <label className="label">Email</label>
                <input className="input" value={email} onChange={e => setEmail(e.target.value)} placeholder="tu@mail.com" />
                <label className="label">Contraseña</label>
                <input className="input" type="password" value={pass} onChange={e => setPass(e.target.value)} placeholder="••••••" />
                <button className="btn" type="submit">Entrar</button>
            </form>
        </div>
    )
}