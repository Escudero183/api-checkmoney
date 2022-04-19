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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.checkmoney.config.exception.ApiCheckMoneyException;
import com.app.checkmoney.model.Divisa;
import com.app.checkmoney.repository.UbigeoRepository;
import com.app.checkmoney.service.DivisaService;
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
	
	@Autowired
	private DivisaService divisaService;
	
	/* Servicios para la Entidad Divisa */
	@ApiOperation(value = "Crea una Divisa", authorizations = {@Authorization(value = "apiKey") })	
	@PostMapping(value = "/divisa")
	public ResponseEntity<?> saveDivisa(@RequestBody Divisa divisa, HttpServletRequest request){
		HashMap<String, Object> result = new HashMap<>();
		divisa.setEstado(true);
		Divisa data = divisaService.insert(divisa);
		
		
		result.put("success", true);
		result.put("message", "Se ha registrado correctamente.");
		result.put("result", data);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Lista todas las Divisas", authorizations = {@Authorization(value = "apiKey") })
	@GetMapping(value = "/divisa")	
	public ResponseEntity<?> findAllDivisa(
			@RequestParam(value = "tipo", required = false, defaultValue = "grilla") String tipo,
			@RequestParam(value = "query", required = false, defaultValue = "") String query,
			@RequestParam(value = "page", required = false, defaultValue = "-1") int page,
			@RequestParam(value = "limit", required = false, defaultValue = "-1") int limit,
			@RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy,
			HttpServletRequest request) {
		
		if(tipo.equals("grilla")) {
			int maxPage = 10;
			
			if (page == -1 && limit == -1 && "".equals(sortBy)) {
				page = 1;
				limit = maxPage;		
			}else if (limit != -1 && page == -1) {
				page = 1;
			} else if (page != -1 && limit == -1) {
				limit = maxPage;
			}
			
			return new ResponseEntity<>(divisaService.findAll(query, page, limit, sortBy), HttpStatus.OK);
		}else {			
			return new ResponseEntity<>(divisaService.findAll(query, sortBy), HttpStatus.OK);
		}
	}
	
	@ApiOperation(value = "Obtiene datos de una Divisa", authorizations = { @Authorization(value = "apiKey")})
	@GetMapping(value = "/divisa/{idDivisa}")
	public ResponseEntity<?> findDivisa(@PathVariable(value = "idDivisa") Integer idDivisa, HttpServletRequest request) {
		HashMap<String, Object> result = new HashMap<>();
		Divisa data = divisaService.findById(idDivisa);
		if(data == null) {
			result.put("success", false);
			result.put("message", "No existe la Divisa con código: " + idDivisa);
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		
		result.put("success", true);
		result.put("message", "Se ha encontrado el registro.");
		result.put("result", data);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Actualiza una Divisa", authorizations = { @Authorization(value = "apiKey")})
	@PutMapping(value = "/divisa")
	public ResponseEntity<?> updateDivisa (@RequestBody Divisa divisa, HttpServletRequest request) {
		HashMap<String, Object> result = new HashMap<>();
		Divisa data = divisaService.findById(divisa.getIdDivisa());
		if(data == null) {
			result.put("success", false);
			result.put("message", "No existe la Divisa con código: " + divisa.getIdDivisa());
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		try {
			divisa.setEstado(true);
			divisaService.update(divisa);
			result.put("success", true);
			result.put("message", "Se ha actualizado los datos del registro.");
			result.put("result", divisa);
			return new ResponseEntity<>(result, HttpStatus.OK);
			
		} catch (Exception ex) {
			return new ResponseEntity<>(new ApiCheckMoneyException(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}			
	}
	
	@ApiOperation(value = "Elimina una Divisa", authorizations = { @Authorization(value = "apiKey")})
	@DeleteMapping(value = "/divisa/{idDivisa}")
	public ResponseEntity<?> deleteDivisa (@PathVariable(value = "idDivisa") Integer idDivisa, HttpServletRequest request){
		HashMap<String, Object> result = new HashMap<>();
		Divisa data = divisaService.findById(idDivisa);
		if(data == null) {
			result.put("success", false);
			result.put("message", "No existe la Divisa con código: " + idDivisa);
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		try {
			data.setEstado(false);
			divisaService.delete(data);
			result.put("success", true);
			result.put("message", "Se ha eliminado los datos del registro.");
			result.put("result", data);
			return new ResponseEntity<>(result, HttpStatus.OK);
			
		} catch (Exception ex) {
			return new ResponseEntity<>(new ApiCheckMoneyException(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	/* Fin Servicios para la Entidad Divisa */
	
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
