/**
 * 
 */
package com.app.checkmoney.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author Linygn Escudero
 *
 */
@Data
@Entity
@Table(name = "sistema", schema = "seguridad")
public class Sistema {
	
	@Id
	@Column(name = "sistema_id")
	@SequenceGenerator(name = "sistemas_generator_seq", sequenceName = "seguridad.sistemas_sistemas_id_seq", allocationSize = 1)
	@GeneratedValue(generator = "sistemas_generator_seq", strategy = GenerationType.SEQUENCE)
	private Integer idSistema;
	
	private String nombre;
	
	private String pathApi;
	
	private String icono;
	
	private boolean estado;

}
