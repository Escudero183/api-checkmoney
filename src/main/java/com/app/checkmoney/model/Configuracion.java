package com.app.checkmoney.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Entity
@Table(name = "configuracion", schema = "seguridad")
@ApiModel("Configuracion")
public class Configuracion {
	
	@Id
	@SequenceGenerator(name = "configuracion_generator_seq", sequenceName = "seguridad.configuracion_id_configuracion_seq", allocationSize = 1)
	@GeneratedValue(generator = "configuracion_generator_seq", strategy = GenerationType.SEQUENCE)
	private Integer idConfiguracion;
	
	@ApiModelProperty(value = "Llave de la configuracion")
	private String llave;
	
	private String valor;

}
