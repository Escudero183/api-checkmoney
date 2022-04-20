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

import com.app.checkmoney.config.audit.AuditModel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(of = { "idToken" })
@Table(name="token", schema = "seguridad")
public class Token extends AuditModel {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "token_id")
	@SequenceGenerator(name = "token_generator_seq", sequenceName = "seguridad.token_token_id_seq", allocationSize = 1)
	@GeneratedValue(generator = "token_generator_seq", strategy = GenerationType.SEQUENCE)
	private Integer idToken;
	private String hash;
	private String hostname;
	@Column(name = "nombre_equipo")
	private String nombreEquipo;
	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;
}
