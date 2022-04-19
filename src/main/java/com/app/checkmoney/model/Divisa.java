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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Linygn Escudero
 *
 */
@Data
@Entity
@Table(name = "divisa", schema = "catalogo")
@ApiModel("Divisa")
public class Divisa {
	
	@Id
	@SequenceGenerator(name = "divisa_generator_seq", sequenceName = "catalogo.divisa_id_divisa_seq", allocationSize = 1)
	@GeneratedValue(generator = "divisa_generator_seq", strategy = GenerationType.SEQUENCE)
	private Integer idDivisa;
	
	@ApiModelProperty(value = "Nombre de la divisa", required = true)
	private String nombre;
	
	private String signo;
	
	private String codigoIso;
	
	private Boolean estado;
	
	@Column(name = "created_at")
	private Date crAt;
	
	@Column(name = "updated_at")
	private Date upAt;
	
	@Column(name = "deleted_at")
	private Date dtAt;
	
	@Column(name = "user_id_created")
	private Integer userCr;
	
	@Column(name = "user_id_mod")
	private Integer userUp;
	
	@Column(name = "user_id_delete")
	private Integer userDt;

}
