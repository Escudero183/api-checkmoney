/**
 * 
 */
package com.app.checkmoney.model;

import java.math.BigDecimal;
import java.util.Date;

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
@Table(name = "tipo_cambio", schema = "proceso")
public class TipoCambio {
	
	@Id
	@SequenceGenerator(name = "tipo_cambio_generator_seq", sequenceName = "proceso.tipo_cambio_id_tipo_cambio_seq", allocationSize = 1)
	@GeneratedValue(generator = "tipo_cambio_generator_seq", strategy = GenerationType.SEQUENCE)
	private Integer idTipoCambio;
	
	private Date fecha;
	
	private String descripcion;
	
	private BigDecimal tasaCambio;
	
	private String formula;
	
	private Boolean vigente;
	
	private Boolean estado;		
	
	@ManyToOne
	@JoinColumn(name = "id_divisa_origen")
	private Divisa divisaOrigen;
	
	@ManyToOne
	@JoinColumn(name = "id_divisa_destino")
	private Divisa divisaDestino;
	
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
