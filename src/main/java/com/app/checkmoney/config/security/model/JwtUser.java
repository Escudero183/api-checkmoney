package com.app.checkmoney.config.security.model;


import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;



public class JwtUser implements UserDetails {

	private static final long serialVersionUID = 1L;
	private final Integer id;
	private final String nombreCompleto;
	private final String nickname;
	private final String email;
	private String password;
	private final boolean estado;
	private final Collection<? extends GrantedAuthority> authorities;
	//private final Contrato contrato;

	/**
	 *
	 * @param id
	 * @param nombreCompleto
	 * @param correo
	 * @param nickname
	 * @param clave
	 * @param authorities
	 * @param enabled
	 */
	public JwtUser(Integer id, String nombreCompleto, String nickname, String email, String password,
			Collection<? extends GrantedAuthority> authorities, boolean enabled) {
		this.id = id;
		this.nickname = nickname;
		this.email = email;
		this.nombreCompleto = nombreCompleto;
		this.password = password;
		this.authorities = authorities;
		this.estado = enabled;
		
		//this.contrato = contrato;
	}

	public Integer getId() {
		return id;
	}

	@Override
	public String getUsername() {
		return nickname;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isEnabled() {
		return estado;
	}

}
