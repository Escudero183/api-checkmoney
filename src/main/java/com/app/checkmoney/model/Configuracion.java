package com.app.checkmoney.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "configuracion", schema = "seguridad")
public class Configuracion {
	
	@Id
	@SequenceGenerator(name = "configuracion_generator_seq", sequenceName = "seguridad.configuracion_id_configuracion_seq", allocationSize = 1)
	@GeneratedValue(generator = "configuracion_generator_seq", strategy = GenerationType.SEQUENCE)
	private Integer idConfiguracion;
	
	private String llave;
	
	private String valor;

}
