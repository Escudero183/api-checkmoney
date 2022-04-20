package com.app.checkmoney.config.security.service;


import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.checkmoney.config.security.token.JwtTokenUtil;
import com.app.checkmoney.model.Usuario;
import com.app.checkmoney.repository.UsuarioRepository;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;


@Service
public class SecurityService {

	/*@Autowired
	private RolRepository rolRepository;

	@Autowired
	private PermisoRepository permisoRepository;*/

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private JwtTokenUtil tokenUtil;

	public boolean hasAccess(String solicitud, String accion, HttpServletRequest request)
			throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException,
			IllegalArgumentException, UnsupportedEncodingException {
		String requestHeader = request.getHeader("Authorization");
		String token = requestHeader.substring(7);
		String username = tokenUtil.getUsernameFromToken(token);
		Usuario usuario = usuarioRepository.findByLogin(username);
		//List<Rol> roles = rolRepository.findAllByUsuario(usuario.getIdUsuario());

		/*List<Perfil> perfils = new ArrayList<Perfil>();
		// primero comparamos los roles que que tienen rango de fechas y que esten
		// habilitados
		for (Rol rol : roles) {
			if (rol.getRangoFechas()) {
				// si fechaInicio<=hoy() && fechaTermino>=hoy() entonces addPerfil
				if ((rol.getFechaInicio().compareTo(new Date()) == -1)
						|| (rol.getFechaInicio().compareTo(new Date()) == 0)
								&& (rol.getFechaTermino().compareTo(new Date()) == 1)
						|| (rol.getFechaTermino().compareTo(new Date()) == 0)) {
					perfils.add(rol.getPerfil());
				}
			} else {
				perfils.add(rol.getPerfil());
			}
		}
		List<Permiso> resultPermisos = new ArrayList<Permiso>();
		for (Perfil perfil : perfils) {
			List<Permiso> permisos = permisoRepository.findAllByPerfil(perfil.getIdPerfil());
			for (Permiso permiso : permisos) {
				if (permiso.isEstado())
					resultPermisos.add(permiso);
			}
		}

		boolean existe = false;
		for (Permiso permiso : resultPermisos) {
			if (permiso.getRecurso().getAccion().equals(accion)
					&& permiso.getRecurso().getSolicitud().equals(solicitud)) {
				existe = true;
			}
		}*/
		return true;
	}
}
