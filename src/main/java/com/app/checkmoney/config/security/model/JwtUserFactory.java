package com.app.checkmoney.config.security.model;



import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.app.checkmoney.model.Usuario;


public final class JwtUserFactory {

	private JwtUserFactory() {
	}

	public static JwtUser create(Usuario user, List<GrantedAuthority> authorities) {
		return new JwtUser(user.getIdUsuario(), user.getLogin(), user.getLogin(),
				user.getEmail(), user.getPassword(), authorities, true);
	}

}