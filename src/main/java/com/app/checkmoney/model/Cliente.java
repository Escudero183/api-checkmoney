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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

/**
 * @author Linygn Escudero
 *
 */
@Data
@Entity
@Table(name = "cliente", schema = "proceso")
public class Cliente {
	
	@Id
	@SequenceGenerator(name = "cliente_generator_seq", sequenceName = "proceso.cliente_id_cliente_seq", allocationSize = 1)
	@GeneratedValue(generator = "cliente_generator_seq", strategy = GenerationType.SEQUENCE)
	private Integer idCliente;
	
	private String nroDocumento;
	
	private String nombre;
	
	private String apellidoPaterno;
	
	private String apellidoMaterno;
	
	private String razonSocial;	
	
	private String direccion;
	
	private String celular;
	
	private String correo;
	
	private Boolean estado;
	
	@ManyToOne
	@JoinColumn(name = "id_tipo_documento")
	private TipoDocumento tipoDocumento;
	
	@ManyToOne
	@JoinColumn(name = "id_ubigeo")
	private Ubigeo ubigeo;
	
	@Transient
	private IUbigeo ubigeoAll;
	
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
