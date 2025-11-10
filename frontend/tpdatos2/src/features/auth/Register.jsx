import React, { useState } from 'react'
import './Register.css'
import { Link } from 'react-router-dom'
import { useNavigate } from 'react-router-dom'
import { useLocation } from 'react-router-dom'

export default function Register() {
	const [nombre, setNombre] = useState('')
	const [email, setEmail] = useState('')
	const [password, setPassword] = useState("");
	const [confirmPassword, setConfirmPassword] = useState("");
	const [showPass, setShowPass] = useState(false)
	const [error, setError] = useState("");
	const [success, setSuccess] = useState(false);

	const navigate = useNavigate();
	const location = useLocation();

	const validateEmail = (email) => {
		const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
		return emailRegex.test(email);
	};

	const validatePassword = (password) => {
		const passwordRegex = /^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?":{}|<>]).{8,}$/;
		return passwordRegex.test(password);
	};

	const validateName = (name) => {
		const nameRegex = /^[A-Za-z\s]{1,30}$/;
		return nameRegex.test(name);
	};

	const submit = async (e) => {
		e.preventDefault();

		if (!validateName(name)) {
			setError("Ingrese un nombre válido.");
			return;
		}
		if (!validateEmail(email)) {
			setError("Por favor, ingresa un correo electrónico válido.");
			return;
		}
		if (!validatePassword(password)) {
			setError("La contraseña debe tener al menos 8 caracteres, una letra mayúscula y un carácter especial.");
			return;
		}
		if (password !== confirmPassword) {
			setError("Las contraseñas no coinciden.");
			return;
		}

		setError("");

		const usuarioARegistrar = {
			nombreCompleto: nombre,
			email,
			password,
			rol: "USUARIO"
		};

		try {
			const res = await fetch("http://localhost:8082/auth/register", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify(usuarioARegistrar),
			});

			if (!res.ok) {
				const errorData = await res.json();
				setError(errorData.message || "Error al registrar el usuario.");
				return;
			}

			setSuccess(true);
			const redirectTo = location.state?.from?.pathname || "/";
			setTimeout(() => navigate(redirectTo), 2000);
		} catch (err) {
			console.error(err);
			setError("No se pudo conectar con el servidor.");
		}
	}

	return (
		<div className="register-wrap">
			<div className="register-card card">
				<div className="register-head">
					<div className="register-brand-circle">TP</div>
					<div className="register-title">
						<h1>Crear cuenta</h1>
						<p className="muted">Por favor, registrate para comenzar</p>
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
								value={password}
								onChange={(e) => setPassword(e.target.value)}
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
							value={confirmPassword}
							onChange={(e) => setConfirmPassword(e.target.value)}
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
