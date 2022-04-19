/**
 * 
 */
package com.app.checkmoney.controller.v1;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.app.checkmoney.repository.UbigeoRepository;
import com.app.checkmoney.service.TipoDocumentoService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

/**
 * @author Linygn Escudero
 *
 */
@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api/v1/catalogo", produces = { MediaType.APPLICATION_JSON_VALUE })
public class CatalogoController {
	
	@Autowired
	private TipoDocumentoService tipoDocumentoService;
	
	@Autowired
	private UbigeoRepository ubigeoRepository;
	
	/* Servicios para la Entidad Tipo de Documento */
	@ApiOperation(value = "Lista todas los Tipos de Documentos", authorizations = {@Authorization(value = "apiKey") })
	@GetMapping(value = "/tipo_documento")	
	public ResponseEntity<?> findAllTipoDocumento(HttpServletRequest request) {		
		return new ResponseEntity<>(tipoDocumentoService.findAll(), HttpStatus.OK);
	}
	/* Fin Servicios para la Entidad Tipo de Documento */
	
	/* Servicios para la Entidad Ubigeo */
	@ApiOperation(value = "Lista Ubigeos", authorizations = {@Authorization(value = "apiKey") })
	@GetMapping(value = "/ubigeo")	
	public ResponseEntity<?> findUbigeos(
			@RequestParam(value = "get", required = false, defaultValue = "dpto") String get,			
			@RequestParam(value = "codUbigeo", required = false, defaultValue = "") String codUbigeo,
			HttpServletRequest request) {
		HashMap<String, Object> result = new HashMap<>();
		
		if(!get.equals("dpto") && !get.equals("prov") && !get.equals("dist")) {
			result.put("success", false);
			result.put("message", "El parámetro get puede recibir: dpto, prov o dist");
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		
		if(get.equals("prov")) {
			if(codUbigeo.length() == 2) {
				return new ResponseEntity<>(ubigeoRepository.findProvs(codUbigeo), HttpStatus.OK);
			}else {
				result.put("success", false);
				result.put("message", "El codUbigeo debe contener sólo dos dígitos, pertenecientes al Departamento");
				return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
			}
		}
		
		if(get.equals("dist")) {
			if(codUbigeo.length() == 4) {
				return new ResponseEntity<>(ubigeoRepository.findDists(codUbigeo), HttpStatus.OK);
			}else {
				result.put("success", false);
				result.put("message", "El codUbigeo debe contener sólo cuatro dígitos, pertenecientes a la Provincia");
				return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
			}
		}
		
		
		return new ResponseEntity<>(ubigeoRepository.findDptos(), HttpStatus.OK);
	}
	/* Fin Servicios para la Entidad Ubigeo */
}
