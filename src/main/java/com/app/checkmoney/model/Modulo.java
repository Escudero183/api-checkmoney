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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Linygn Escudero
 *
 */
@Data
@Entity
@Table(name = "modulo", schema = "seguridad")
@ApiModel("Modulo")
public class Modulo {
	
	@Id
	@Column(name = "modulo_id")
	@SequenceGenerator(name = "modulo_generator_seq", sequenceName = "seguridad.modulo_modulo_id_seq", allocationSize = 1)
	@GeneratedValue(generator = "modulo_generator_seq", strategy = GenerationType.SEQUENCE)
	private Integer idModulo;
	
	@ApiModelProperty(value = "Nombre del Módulo")
	private String nombre;
	
	private String descripcion;
	
	private String abreviatura;
	
	private String url;
	
	private String icono;
	
	private Integer nroOrden;
	
	private Integer moduloPadre;
	
	private boolean destacadoFlag;
	
	private String destacadoColor;
	
	private String esquema;
	
	private boolean estado;
	
	@ManyToOne
	@JoinColumn(name = "sistema_id")
	private Sistema sistema;

}
