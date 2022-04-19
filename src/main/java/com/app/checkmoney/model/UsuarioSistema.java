/**
 * 
 */
package com.app.checkmoney.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * @author Linygn Escudero
 *
 */
@Data
@Entity
@Table(name = "usuario_sistema", schema  = "seguridad")
public class UsuarioSistema {
	
	@Id
	@Column(name = "usuario_sistema_id")
	@SequenceGenerator(name = "usuario_sistema_generator_seq", sequenceName = "seguridad.usuario_sistema_usuario_sistema_id_seq", allocationSize = 1)
	@GeneratedValue(generator = "usuario_sistema_generator_seq", strategy = GenerationType.SEQUENCE)
	private Integer idUsuarioSistema;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name = "sistema_id")
	private Sistema sistema;
	
	@ManyToOne
	@JoinColumn(name = "perfil_id")
	private Perfil perfil;

}
