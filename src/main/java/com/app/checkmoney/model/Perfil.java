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

import lombok.Data;

/**
 * @author Linygn Escudero
 *
 */

@Data
@Entity
@Table(name = "perfil", schema = "seguridad")
public class Perfil {
	
	@Id
	@Column(name = "perfil_id")
	@SequenceGenerator(name = "perfil_generator_seq", sequenceName = "seguridad.perfil_perfil_id_seq", allocationSize = 1)
	@GeneratedValue(generator = "perfil_generator_seq", strategy = GenerationType.SEQUENCE)
	private Integer idPerfil;
	
	private String nombre;
	
	private String icono;
	
	private boolean estado;
	
	@ManyToOne
	@JoinColumn(name = "sistema_id")
	private Sistema sistema;

}
