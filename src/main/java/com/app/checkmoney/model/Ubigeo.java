/**
 * 
 */
package com.app.checkmoney.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * @author Linygn Escudero
 *
 */
@Data
@Entity
@Table(name = "ubigeo", schema = "catalogo")
public class Ubigeo {
	
	@Id
	@Column(name = "id_ubigeo")
	private String idUbigeo;
	
	private String descripcion;

}
