/**
 * 
 */
package com.app.checkmoney.model;

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
@Table(name = "cliente", schema = "comercial")
public class Cliente {
	
	@Id
	@SequenceGenerator(name = "cliente_generator_seq", sequenceName = "comercial.cliente_id_cliente_seq", allocationSize = 1)
	@GeneratedValue(generator = "cliente_generator_seq", strategy = GenerationType.SEQUENCE)
	private Integer idCliente;
	
	private String tipoPersona;
	
	private String tipoProducto;
	
	private String documento;
	
	private String razonSocial;
	
	private String representanteLegal;
	
	private String latitud;
	
	private String longitud;
	
	private String direccion;
	
	private String foto;
	
	private String codigoOsinergmin;
	
	private Integer capacidadAutorizada;
	
	private Integer capacidadGranel;
	
	private Integer capacidadActualPorc;
	
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

}
