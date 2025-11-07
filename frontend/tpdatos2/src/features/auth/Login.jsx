import React, { useState } from 'react';
import './Login.css';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from './authContext.jsx';
import { fetchApi } from '../../app/api.js';

export default function Login() {
    const [email, setEmail] = useState('');
    const [pass, setPass] = useState('');
    const [showPass, setShowPass] = useState(false);
    const [error, setError] = useState('');
    const { login, user } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();

    const submit = async (e) => {
        e.preventDefault();
        setError('');
        try {
            // ajusta el path si tu back es /auth/login sin /api
            const data = await fetchApi('/auth/login', {
                auth: false,
                method: 'POST',
                body: JSON.stringify({ email, password: pass }),
            });
            const token = data?.token;
            if (!token) { setError('Respuesta inválida del servidor.'); return; }

            login(token);

            // redirección: si ADMIN -> /admin, si no -> a la ruta previa o "/"
            const dest = (user?.role === 'ADMIN')
                ? '/admin'
                : (location.state?.from?.pathname || '/');
            navigate(dest, { replace: true });
        } catch (err) {
            setError(err.message === 'UNAUTHORIZED' ? 'Credenciales incorrectas.' : 'No se pudo conectar.');
        }
    };

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
                    {error && <div className="form-error">{error}</div>}
                    <button className="btn btn-primary btn-full" type="submit">Entrar</button>
                </form>
                <p className="login-foot muted">
                    ¿No tenés cuenta? <Link className="link" to="/register">Crear cuenta</Link>
                </p>
            </div>
        </div>
    );
}
