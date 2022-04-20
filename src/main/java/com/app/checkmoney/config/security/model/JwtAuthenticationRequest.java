package com.app.checkmoney.config.security.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("Autentificación")
public final class JwtAuthenticationRequest {

	@ApiModelProperty(value = "representa el nickname del usuario por defecto es el correo", required = true)
	private String username;
	@ApiModelProperty(value = "representa la contraseña del usuario", required = true)
	private String password;

	public JwtAuthenticationRequest() {
		super();
	}

	public JwtAuthenticationRequest(String username, String password) {
		this.setUsername(username);
		this.setPassword(password);
	}

	public String getUsername() {
		return this.username;
	}

	public final void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
