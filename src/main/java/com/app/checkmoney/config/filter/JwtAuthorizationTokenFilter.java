package com.app.checkmoney.config.filter;



import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.app.checkmoney.config.security.token.JwtTokenUtil;
import com.app.checkmoney.model.Usuario;
import com.app.checkmoney.service.TokenService;
import com.app.checkmoney.service.UsuarioService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;



@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {

	/**
	 * Interfaz donde almacena los datos del usuario para el token
	 */
	private final UserDetailsService userDetailsService;
	/**
	 * Utilidad del token basado e jjwt
	 */
	private final JwtTokenUtil jwtTokenUtil;
	private final String tokenHeader;

	// repositorios
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private TokenService tokenService;

	/**
	 * Constructor
	 *
	 * @param userDetailsService
	 * @param jwtTokenUtil
	 */
	public JwtAuthorizationTokenFilter(UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil) {
		this.userDetailsService = userDetailsService;
		this.jwtTokenUtil = jwtTokenUtil;
		this.tokenHeader = "Authorization";
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// Obtenemos la cabecera
		final String requestHeader = request.getHeader(this.tokenHeader);

		String username = null;
		String authToken = null;
		
		// verificamos que contenga el token
		if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
			authToken = requestHeader.substring(7);
			
			try {
				username = jwtTokenUtil.getUsernameFromToken(authToken);
				Usuario usuario = usuarioService.findByLogin(username.toUpperCase());
				
				if (!usuario.isEstado()) {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					return;
				}
				boolean found = tokenService.checkTokenByUsuario(authToken, usuario.getIdUsuario(),
						request.getHeader("User-Agent"));			
				if (!found) {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					return;
				}
			} catch (IllegalArgumentException | ExpiredJwtException | SignatureException | AccessDeniedException e) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			// obtenemos el usuario de la base de datos
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			if (jwtTokenUtil.validateToken(authToken, userDetails)) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		filterChain.doFilter(request, response);
	}

}
