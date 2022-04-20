/**
 * 
 */
package com.app.checkmoney.model;

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
@Table(name = "tipo_documento", schema = "catalogo")
public class TipoDocumento {
	
	@Id
	@SequenceGenerator(name = "tipo_documento_generator_seq", sequenceName = "catalogo.tipo_documento_id_tipo_documento_seq", allocationSize = 1)
	@GeneratedValue(generator = "tipo_documento_generator_seq", strategy = GenerationType.SEQUENCE)
	private Integer idTipoDocumento;
	
	private String nombre;
	
	private String abreviatura;
	
	private Integer digitos;
	
	private Boolean estado;	

}
