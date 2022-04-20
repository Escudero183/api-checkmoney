/**
 * 
 */
package com.app.checkmoney.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Linygn Escudero
 *
 */
@Data
@Entity
@Table(name = "usuario", schema  = "seguridad")
@EqualsAndHashCode(callSuper = false)
public class Usuario {
	
	@Id
	@Column(name = "usuario_id")
	@SequenceGenerator(name = "usuario_generator_seq", sequenceName = "seguridad.usuario_usuario_id_seq", allocationSize = 1)
	@GeneratedValue(generator = "usuario_generator_seq", strategy = GenerationType.SEQUENCE)
	private Integer idUsuario;
	
	private String login;
	
	private String password;
	
	private Date fechaAlta;
	
	private Date fechaBaja;
	
	private String estadoCuenta;
	
	private String email;
	
	private boolean estado;

}
