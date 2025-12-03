import React, { useState } from 'react';
import './Login.css';
import { useNavigate, useLocation } from 'react-router-dom';
import { jwtDecode } from "jwt-decode";
import { Link } from 'react-router-dom';

export default function Login() {
	const [email, setEmail] = useState('');
	const [pass, setPass] = useState('');
	const [showPass, setShowPass] = useState(false);
	const [error, setError] = useState('');
	const navigate = useNavigate();
	const location = useLocation();

	const validateEmail = (email) => {
		const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
		return emailRegex.test(email);
	};

	const validatePassword = (pass) => {
		const passwordRegex = /^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?":{}|<>]).{8,}$/;
		return passwordRegex.test(pass);
	};


	const submit = async (e) => {
		e.preventDefault();
		if (!validateEmail(email)) {
			setError("Por favor, ingresa un correo electrónico válido.");
			return;
		}

		if (!validatePassword(pass)) {
			setError("La contraseña debe tener al menos 8 caracteres, una letra mayúscula y un carácter especial.");
			return;
		}
		setError("");

		try {
			const response = await fetch("http://localhost:8082/auth/login", {
				method: "POST",
				headers: {
					"Content-Type": "application/json",
				},
				body: JSON.stringify({ email, pass }),
			});

			if (!response.ok) {
				setError("Credenciales incorrectas.");
				return;
			}

			const { token } = await response.json();
			localStorage.setItem("token", token);
			localStorage.setItem("userEmail", email);
			onSubmit(token);

			const { rol } = jwtDecode(token);

			if (rol === "ADMIN") {
				navigate("/admin");
			} else {
				const redirectTo = location.state?.from?.pathname || "/";
				navigate(redirectTo);
			}

		} catch (err) {
			console.error("Error de conexión:", err);
			setError("No se pudo conectar al servidor.");
		}
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
