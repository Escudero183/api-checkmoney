package com.app.checkmoney.config.security.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.checkmoney.config.security.model.JwtUserFactory;
import com.app.checkmoney.model.Usuario;
import com.app.checkmoney.repository.UsuarioRepository;

@Service
public class AuthService implements UserDetailsService {
	private Logger logger = LoggerFactory.getLogger(AuthService.class);

	@Autowired
	private UsuarioRepository usuarioRepository;

	/*
	 * @Autowired private ContratoUsuarioRepository contratoUsuarioRepository;
	 */

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		if (username == null) {
			logger.error("No Existe el usuario");
		}

		String nickname = username;
		Usuario usuario = usuarioRepository.findByLogin(nickname.toUpperCase());

		if (usuario == null) {
			throw new UsernameNotFoundException(String.format("No existe el usuario '%s'.", username));
		} else {
			/*
			 * Contrato contrato =
			 * contratoUsuarioRepository.findContratoByUsuario(usuario.getIdUsuario()); if
			 * (contrato != null) { usuario.setContrato(contrato); } List<Rol>
			 * perfilUsuarios = rolRepository.findAllByUsuario(usuario.getIdUsuario());
			 * List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(); for
			 * (Rol perfilUsuario : perfilUsuarios) { authorities.add(new
			 * SimpleGrantedAuthority(perfilUsuario.getPerfil().getNombre())); }
			 */	
			String perfil = "Admin";
			
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority(perfil));
			return JwtUserFactory.create(usuario, authorities);
		}
	}

}
