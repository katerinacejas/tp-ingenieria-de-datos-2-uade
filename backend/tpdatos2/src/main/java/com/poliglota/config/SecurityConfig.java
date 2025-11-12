package com.poliglota.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;
import com.poliglota.security.JwtAuthenticationFilter;
import com.poliglota.security.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final CustomUserDetailsService customUserDetailsService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.cors(Customizer.withDefaults()) // Aplica configuración CORS
				.csrf(CsrfConfigurer::disable) // CSRF gestionado selectivamente (stateless API)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						// Público solo auth
						.requestMatchers("/auth/**").permitAll()

						// Dashboard (resumen)
						.requestMatchers(HttpMethod.GET, "/dashboard/**")
							.hasAnyRole("USUARIO","MANTENIMIENTO","ADMIN")

						// Facturación / pagos / cuentas / movimientos: lectura para todos los roles autenticados
						.requestMatchers(HttpMethod.GET,
							"/api/invoices/**", "/api/payments/**",
							"/api/accounts/**", "/api/account-movements/**")
							.hasAnyRole("USUARIO","MANTENIMIENTO","ADMIN")
						// Registrar pago
						.requestMatchers(HttpMethod.POST, "/api/payments/**")
							.hasAnyRole("USUARIO","MANTENIMIENTO")

						// Sensores / mediciones
						.requestMatchers(HttpMethod.GET, "/api/measurements/**", "/api/sensors/**")
							.hasAnyRole("USUARIO","MANTENIMIENTO","ADMIN")
						.requestMatchers(HttpMethod.POST, "/api/measurements/**", "/api/sensors/**")
							.hasRole("MANTENIMIENTO")
						.requestMatchers(HttpMethod.PUT, "/api/measurements/**", "/api/sensors/**")
							.hasRole("MANTENIMIENTO")
						.requestMatchers(HttpMethod.PATCH, "/api/measurements/**", "/api/sensors/**")
							.hasRole("MANTENIMIENTO")
						.requestMatchers(HttpMethod.DELETE, "/api/measurements/**", "/api/sensors/**")
							.hasRole("MANTENIMIENTO")

						// Alertas
						.requestMatchers(HttpMethod.GET, "/api/alerts/**")
							.hasAnyRole("USUARIO","MANTENIMIENTO","ADMIN")
						.requestMatchers(HttpMethod.POST, "/api/alerts/**")
							.hasRole("MANTENIMIENTO")
						.requestMatchers(HttpMethod.PUT, "/api/alerts/**")
							.hasRole("MANTENIMIENTO")
						.requestMatchers(HttpMethod.PATCH, "/api/alerts/**")
							.hasRole("MANTENIMIENTO")
						.requestMatchers(HttpMethod.DELETE, "/api/alerts/**")
							.hasRole("MANTENIMIENTO")

						// Procesos
						.requestMatchers(HttpMethod.GET, "/api/processes/**")
							.hasAnyRole("USUARIO","MANTENIMIENTO","ADMIN")
						.requestMatchers(HttpMethod.POST, "/api/processes/**")
							.hasAnyRole("USUARIO","MANTENIMIENTO")
						.requestMatchers(HttpMethod.PUT, "/api/processes/**")
							.hasRole("MANTENIMIENTO")
						.requestMatchers(HttpMethod.PATCH, "/api/processes/**")
							.hasRole("MANTENIMIENTO")
						.requestMatchers(HttpMethod.DELETE, "/api/processes/**")
							.hasRole("MANTENIMIENTO")

						// Mensajes y grupos
						.requestMatchers("/api/messages/**", "/api/groups/**")
							.hasAnyRole("USUARIO","MANTENIMIENTO","ADMIN")

						// Administración (alta de personal de mantenimiento, etc.)
						.requestMatchers("/admin/**", "/accounts/**/maintenance/**", "/users/**/maintenance/**")
							.hasRole("ADMIN")
						.requestMatchers(HttpMethod.POST, "/accounts/**", "/users/**")
							.hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/accounts/**", "/users/**")
							.hasRole("ADMIN")
						.requestMatchers(HttpMethod.PATCH, "/accounts/**", "/users/**")
							.hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/accounts/**", "/users/**")
							.hasRole("ADMIN")

						// Todo lo demás autenticado
						.anyRequest().authenticated()
					)

				.authenticationProvider(authenticationProvider())
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(customUserDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("http://localhost:8080"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("*"));
		config.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}