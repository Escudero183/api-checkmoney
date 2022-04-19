/**
 * 
 */
package com.app.checkmoney.controller.v1;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.app.checkmoney.config.exception.RestException;
import com.app.checkmoney.config.security.model.JwtUser;
import com.app.checkmoney.model.Cliente;
import com.app.checkmoney.model.TipoCambio;
import com.app.checkmoney.service.ClienteService;
import com.app.checkmoney.service.TipoCambioService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

/**
 * @author Linygn Escudero
 *
 */
@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api/v1/proceso", produces = { MediaType.APPLICATION_JSON_VALUE })
public class ProcesoController {
	
	@Autowired
	private TipoCambioService tipoCambioService;
	
	@Autowired
	private ClienteService clienteService;
	
	/* Servicios para la Entidad TipoCambio */
	@ApiOperation(value = "Crea un Tipo de Cambio", authorizations = {@Authorization(value = "apiKey") })	
	@PostMapping(value = "/tipo_cambio")
	public ResponseEntity<?> saveTipoCambio(@RequestBody TipoCambio tipoCambio, HttpServletRequest request){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Integer idUser = 0;
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			JwtUser userDetails = (JwtUser) auth.getPrincipal();
			idUser = userDetails.getId();

		} else {
			return new ResponseEntity<>(new RestException("No autorizado"), HttpStatus.UNAUTHORIZED);
		}
		
		HashMap<String, Object> result = new HashMap<>();
		
		List<TipoCambio> existe = tipoCambioService.findVigentes(tipoCambio.getFecha(), tipoCambio.getDivisaOrigen().getIdDivisa(), tipoCambio.getDivisaDestino().getIdDivisa());
		if(!existe.isEmpty()) {
			result.put("success", false);
			result.put("message", "Ya existe un registro vigente del tipo de cambio con la fecha asignada. Le recomendamos editar el registro.");
			return new ResponseEntity<>(result, HttpStatus.CONFLICT); 
		}
		
		tipoCambio.setEstado(true);
		tipoCambio.setUserCr(idUser);
		TipoCambio data = tipoCambioService.insert(tipoCambio);
		
		
		result.put("success", true);
		result.put("message", "Se ha registrado correctamente.");
		result.put("result", data);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Lista todas los Tipos de Cambio", authorizations = {@Authorization(value = "apiKey") })
	@GetMapping(value = "/tipo_cambio")	
	public ResponseEntity<?> findAllTipoCambio(
			@RequestParam(value = "fecha", required = false, defaultValue = "") String fecha,
			@RequestParam(value = "vigente", required = false, defaultValue = "true") Boolean vigente,
			@RequestParam(value = "tipo", required = false, defaultValue = "grilla") String tipo,
			@RequestParam(value = "query", required = false, defaultValue = "") String query,
			@RequestParam(value = "page", required = false, defaultValue = "-1") int page,
			@RequestParam(value = "limit", required = false, defaultValue = "-1") int limit,
			@RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy,
			HttpServletRequest request) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Integer idUser = 0;
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			JwtUser userDetails = (JwtUser) auth.getPrincipal();
			idUser = userDetails.getId();

		} else {
			return new ResponseEntity<>(new RestException("No autorizado"), HttpStatus.UNAUTHORIZED);
		}
		
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
			
			return new ResponseEntity<>(tipoCambioService.findAll(fecha, vigente, query, page, limit, sortBy), HttpStatus.OK);
		}else {			
			return new ResponseEntity<>(tipoCambioService.findAll(query, sortBy), HttpStatus.OK);
		}
	}
	
	@ApiOperation(value = "Obtiene datos de un Tipo de Cambio", authorizations = { @Authorization(value = "apiKey")})
	@GetMapping(value = "/tipo_cambio/{idTipoCambio}")
	public ResponseEntity<?> findTipoCambio(@PathVariable(value = "idTipoCambio") Integer idTipoCambio, HttpServletRequest request) {
		HashMap<String, Object> result = new HashMap<>();
		TipoCambio data = tipoCambioService.findById(idTipoCambio);
		if(data == null) {
			result.put("success", false);
			result.put("message", "No existe el Tipo de Cambio con código: " + idTipoCambio);
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		
		result.put("success", true);
		result.put("message", "Se ha encontrado el registro.");
		result.put("result", data);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Actualiza un Tipo de Cambio", authorizations = { @Authorization(value = "apiKey")})
	@PutMapping(value = "/tipo_cambio")
	public ResponseEntity<?> updateTipoCambio (@RequestBody TipoCambio tipoCambio, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Integer idUser = 0;
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			JwtUser userDetails = (JwtUser) auth.getPrincipal();
			idUser = userDetails.getId();

		} else {
			return new ResponseEntity<>(new RestException("No autorizado"), HttpStatus.UNAUTHORIZED);
		}
		
		HashMap<String, Object> result = new HashMap<>();
		TipoCambio data = tipoCambioService.findById(tipoCambio.getIdTipoCambio());
		if(data == null) {
			result.put("success", false);
			result.put("message", "No existe el Tipo de Cambio con código: " + tipoCambio.getIdTipoCambio());
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		try {
			tipoCambio.setUpAt(new java.sql.Timestamp(System.currentTimeMillis()));
			tipoCambio.setUserUp(idUser);
			tipoCambio.setEstado(true);
			tipoCambioService.update(tipoCambio);
			result.put("success", true);
			result.put("message", "Se ha actualizado los datos del registro.");
			result.put("result", tipoCambio);
			return new ResponseEntity<>(result, HttpStatus.OK);
			
		} catch (Exception ex) {
			return new ResponseEntity<>(new ApiCheckMoneyException(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}			
	}
	
	@ApiOperation(value = "Elimina un Tipo de Cambio", authorizations = { @Authorization(value = "apiKey")})
	@DeleteMapping(value = "/tipo_cambio/{idTipoCambio}")
	public ResponseEntity<?> deleteTipoCambio (@PathVariable(value = "idTipoCambio") Integer idTipoCambio, HttpServletRequest request){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Integer idUser = 0;
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			JwtUser userDetails = (JwtUser) auth.getPrincipal();
			idUser = userDetails.getId();

		} else {
			return new ResponseEntity<>(new RestException("No autorizado"), HttpStatus.UNAUTHORIZED);
		}
		
		HashMap<String, Object> result = new HashMap<>();
		TipoCambio data = tipoCambioService.findById(idTipoCambio);
		if(data == null) {
			result.put("success", false);
			result.put("message", "No existe el Tipo de Cambio con código: " + idTipoCambio);
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		try {
			data.setDtAt(new java.sql.Timestamp(System.currentTimeMillis()));
			data.setUserDt(idUser);
			data.setEstado(false);
			tipoCambioService.delete(data);
			result.put("success", true);
			result.put("message", "Se ha eliminado los datos del registro.");
			result.put("result", data);
			return new ResponseEntity<>(result, HttpStatus.OK);
			
		} catch (Exception ex) {
			return new ResponseEntity<>(new ApiCheckMoneyException(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	/* Fin Servicios para la Entidad TipoCambio */
	
	/* Servicios para la Entidad Cliente */
	@ApiOperation(value = "Crea un Cliente", authorizations = {@Authorization(value = "apiKey") })	
	@PostMapping(value = "/cliente")
	public ResponseEntity<?> saveCliente(@RequestBody Cliente cliente, HttpServletRequest request){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Integer idUser = 0;
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			JwtUser userDetails = (JwtUser) auth.getPrincipal();
			idUser = userDetails.getId();

		} else {
			return new ResponseEntity<>(new RestException("No autorizado"), HttpStatus.UNAUTHORIZED);
		}
		
		HashMap<String, Object> result = new HashMap<>();		
		cliente.setEstado(true);
		cliente.setUserCr(idUser);
		Cliente data = clienteService.insert(cliente);
		
		
		result.put("success", true);
		result.put("message", "Se ha registrado correctamente.");
		result.put("result", data);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Lista todas los Clientes", authorizations = {@Authorization(value = "apiKey") })
	@GetMapping(value = "/cliente")	
	public ResponseEntity<?> findAllCliente(
			@RequestParam(value = "tipo", required = false, defaultValue = "grilla") String tipo,
			@RequestParam(value = "query", required = false, defaultValue = "") String query,
			@RequestParam(value = "page", required = false, defaultValue = "-1") int page,
			@RequestParam(value = "limit", required = false, defaultValue = "-1") int limit,
			@RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy,
			HttpServletRequest request) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Integer idUser = 0;
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			JwtUser userDetails = (JwtUser) auth.getPrincipal();
			idUser = userDetails.getId();

		} else {
			return new ResponseEntity<>(new RestException("No autorizado"), HttpStatus.UNAUTHORIZED);
		}
		
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
			
			return new ResponseEntity<>(clienteService.findAll(query, page, limit, sortBy), HttpStatus.OK);
		}else {			
			return new ResponseEntity<>(clienteService.findAll(query, sortBy), HttpStatus.OK);
		}
	}
	
	@ApiOperation(value = "Obtiene datos de un Cliente", authorizations = { @Authorization(value = "apiKey")})
	@GetMapping(value = "/cliente/{idCliente}")
	public ResponseEntity<?> findCliente(@PathVariable(value = "idCliente") Integer idCliente, HttpServletRequest request) {
		HashMap<String, Object> result = new HashMap<>();
		Cliente data = clienteService.findById(idCliente);
		if(data == null) {
			result.put("success", false);
			result.put("message", "No existe el Cliente con código: " + idCliente);
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		
		result.put("success", true);
		result.put("message", "Se ha encontrado el registro.");
		result.put("result", data);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Actualiza un Cliente", authorizations = { @Authorization(value = "apiKey")})
	@PutMapping(value = "/cliente")
	public ResponseEntity<?> updateCliente (@RequestBody Cliente cliente, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Integer idUser = 0;
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			JwtUser userDetails = (JwtUser) auth.getPrincipal();
			idUser = userDetails.getId();

		} else {
			return new ResponseEntity<>(new RestException("No autorizado"), HttpStatus.UNAUTHORIZED);
		}
		
		HashMap<String, Object> result = new HashMap<>();
		Cliente data = clienteService.findById(cliente.getIdCliente());
		if(data == null) {
			result.put("success", false);
			result.put("message", "No existe el Cliente con código: " + cliente.getIdCliente());
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		try {
			cliente.setUpAt(new java.sql.Timestamp(System.currentTimeMillis()));
			cliente.setUserUp(idUser);
			cliente.setEstado(true);
			clienteService.update(cliente);
			result.put("success", true);
			result.put("message", "Se ha actualizado los datos del registro.");
			result.put("result", cliente);
			return new ResponseEntity<>(result, HttpStatus.OK);
			
		} catch (Exception ex) {
			return new ResponseEntity<>(new ApiCheckMoneyException(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}			
	}
	
	@ApiOperation(value = "Elimina un Cliente", authorizations = { @Authorization(value = "apiKey")})
	@DeleteMapping(value = "/cliente/{idCliente}")
	public ResponseEntity<?> deleteCliente (@PathVariable(value = "idCliente") Integer idCliente, HttpServletRequest request){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Integer idUser = 0;
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			JwtUser userDetails = (JwtUser) auth.getPrincipal();
			idUser = userDetails.getId();

		} else {
			return new ResponseEntity<>(new RestException("No autorizado"), HttpStatus.UNAUTHORIZED);
		}
		
		HashMap<String, Object> result = new HashMap<>();
		Cliente data = clienteService.findById(idCliente);
		if(data == null) {
			result.put("success", false);
			result.put("message", "No existe el Cliente con código: " + idCliente);
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		try {
			data.setDtAt(new java.sql.Timestamp(System.currentTimeMillis()));
			data.setUserDt(idUser);
			data.setEstado(false);
			clienteService.delete(data);
			result.put("success", true);
			result.put("message", "Se ha eliminado los datos del registro.");
			result.put("result", data);
			return new ResponseEntity<>(result, HttpStatus.OK);
			
		} catch (Exception ex) {
			return new ResponseEntity<>(new ApiCheckMoneyException(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	/* Fin Servicios para la Entidad Cliente */

}
