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
@Table(name = "transaccion", schema = "proceso")
public class Transaccion {
	
	@Id
	@Column(name = "id_transaccion")
	@SequenceGenerator(name = "transaccion_generator_seq", sequenceName = "proceso.transaccion_id_transaccion_seq", allocationSize = 1)
	@GeneratedValue(generator = "transaccion_generator_seq", strategy = GenerationType.SEQUENCE)
	private Integer idTransaccion;
	
	private Date fechaOperacion;
	
	private BigDecimal montoOrigen;
	
	private BigDecimal montoDestino;
	
	private BigDecimal tasaCambio;
	
	private String formula;
	
	private Boolean estado;
	
	@ManyToOne
	@JoinColumn(name = "id_cliente")
	private Cliente cliente;
	
	@ManyToOne
	@JoinColumn(name = "id_tipo_cambio")
	private TipoCambio tipoCambio;
	
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
