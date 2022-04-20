/**
 * 
 */
package com.app.checkmoney.controller.v1;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
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
import com.app.checkmoney.model.Transaccion;
import com.app.checkmoney.service.ClienteService;
import com.app.checkmoney.service.TipoCambioService;
import com.app.checkmoney.service.TransaccionService;

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
	
	@Autowired
	private TransaccionService transaccionService;
	
	/* Servicios para la Entidad Transaccion */
	@ApiOperation(value = "Crea una Transaccion", authorizations = {@Authorization(value = "apiKey") })	
	@PostMapping(value = "/transaccion")
	public ResponseEntity<?> saveTransaccion(@RequestBody Transaccion transaccion, HttpServletRequest request){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Integer idUser = 0;
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			JwtUser userDetails = (JwtUser) auth.getPrincipal();
			idUser = userDetails.getId();

		} else {
			return new ResponseEntity<>(new RestException("No autorizado"), HttpStatus.UNAUTHORIZED);
		}
		
		HashMap<String, Object> result = new HashMap<>();
		
		Cliente cliente = clienteService.findById(transaccion.getCliente().getIdCliente());
		if(cliente == null) {
			result.put("success", false);
			result.put("message", "No existe el Cliente con código:" + transaccion.getCliente().getIdCliente());
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		
		TipoCambio tipoCambio = tipoCambioService.findById(transaccion.getTipoCambio().getIdTipoCambio());
		if(tipoCambio == null) {
			result.put("success", false);
			result.put("message", "No existe el Tipo de Cambio con código:" + transaccion.getTipoCambio().getIdTipoCambio());
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		
		transaccion.setEstado(true);
		transaccion.setUserCr(idUser);
		Transaccion data = transaccionService.insert(transaccion);
		
		
		result.put("success", true);
		result.put("message", "Se ha registrado correctamente.");
		result.put("result", data);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Lista todas las Transacciones", authorizations = {@Authorization(value = "apiKey") })
	@GetMapping(value = "/transaccion")	
	public ResponseEntity<?> findAllTransaccion(
			@RequestParam(value = "fechaOperacion", required = false, defaultValue = "") String fechaOperacion,
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
			
			return new ResponseEntity<>(transaccionService.findAll(fechaOperacion, query, page, limit, sortBy), HttpStatus.OK);
		}else {			
			return new ResponseEntity<>(transaccionService.findAll(query, sortBy), HttpStatus.OK);
		}
	}
	
	@ApiOperation(value = "Obtiene datos de una Transaccion", authorizations = { @Authorization(value = "apiKey")})
	@GetMapping(value = "/transaccion/{idTransaccion}")
	public ResponseEntity<?> findTransaccion(@PathVariable(value = "idTransaccion") Integer idTransaccion, HttpServletRequest request) {
		HashMap<String, Object> result = new HashMap<>();
		Transaccion data = transaccionService.findById(idTransaccion);
		if(data == null) {
			result.put("success", false);
			result.put("message", "No existe la Transaccion con código: " + idTransaccion);
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		
		result.put("success", true);
		result.put("message", "Se ha encontrado el registro.");
		result.put("result", data);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Actualiza una Transaccion", authorizations = { @Authorization(value = "apiKey")})
	@PutMapping(value = "/transaccion")
	public ResponseEntity<?> updateTransaccion (@RequestBody Transaccion transaccion, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Integer idUser = 0;
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			JwtUser userDetails = (JwtUser) auth.getPrincipal();
			idUser = userDetails.getId();

		} else {
			return new ResponseEntity<>(new RestException("No autorizado"), HttpStatus.UNAUTHORIZED);
		}
		
		HashMap<String, Object> result = new HashMap<>();
		Transaccion data = transaccionService.findById(transaccion.getIdTransaccion());
		if(data == null) {
			result.put("success", false);
			result.put("message", "No existe la Transaccion con código: " + transaccion.getIdTransaccion());
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		try {
			transaccion.setUpAt(new java.sql.Timestamp(System.currentTimeMillis()));
			transaccion.setUserUp(idUser);
			transaccion.setEstado(true);
			transaccionService.update(transaccion);
			result.put("success", true);
			result.put("message", "Se ha actualizado los datos del registro.");
			result.put("result", transaccion);
			return new ResponseEntity<>(result, HttpStatus.OK);
			
		} catch (Exception ex) {
			return new ResponseEntity<>(new ApiCheckMoneyException(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}			
	}
	
	@ApiOperation(value = "Elimina una Transaccion", authorizations = { @Authorization(value = "apiKey")})
	@DeleteMapping(value = "/transaccion/{idTransaccion}")
	public ResponseEntity<?> deleteTransaccion (@PathVariable(value = "idTransaccion") Integer idTransaccion, HttpServletRequest request){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Integer idUser = 0;
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			JwtUser userDetails = (JwtUser) auth.getPrincipal();
			idUser = userDetails.getId();

		} else {
			return new ResponseEntity<>(new RestException("No autorizado"), HttpStatus.UNAUTHORIZED);
		}
		
		HashMap<String, Object> result = new HashMap<>();
		Transaccion data = transaccionService.findById(idTransaccion);
		if(data == null) {
			result.put("success", false);
			result.put("message", "No existe la Transaccion con código: " + idTransaccion);
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		try {
			data.setDtAt(new java.sql.Timestamp(System.currentTimeMillis()));
			data.setUserDt(idUser);
			data.setEstado(false);
			transaccionService.delete(data);
			result.put("success", true);
			result.put("message", "Se ha eliminado los datos del registro.");
			result.put("result", data);
			return new ResponseEntity<>(result, HttpStatus.OK);
			
		} catch (Exception ex) {
			return new ResponseEntity<>(new ApiCheckMoneyException(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	/* Fin Servicios para la Entidad Transaccion */
	
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
		
		if(tipoCambio.getDivisaOrigen().getIdDivisa() == tipoCambio.getDivisaDestino().getIdDivisa()) {
			result.put("success", false);
			result.put("message", "Debe seleccionar divisas diferentes para registrar el tipo de cambio.");
			return new ResponseEntity<>(result, HttpStatus.CONFLICT); 
		}
		
		List<TipoCambio> existe = tipoCambioService.findTCByFecha(tipoCambio.getFecha().toString(), tipoCambio.getDivisaOrigen().getIdDivisa(), tipoCambio.getDivisaDestino().getIdDivisa());
		if(!existe.isEmpty()) {
			result.put("success", false);
			result.put("message", "Ya existe un registro vigente del tipo de cambio con la fecha asignada. Le recomendamos editar el registro.");
			return new ResponseEntity<>(result, HttpStatus.CONFLICT); 
		}
		
		tipoCambio.setEstado(true);
		tipoCambio.setCrAt(new java.sql.Timestamp(System.currentTimeMillis()));
		tipoCambio.setUserCr(idUser);
		tipoCambio.setUpAt(null);
		tipoCambio.setDtAt(null);
		tipoCambio.setUserUp(null);
		tipoCambio.setUserDt(null);
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
	
	@ApiOperation(value = "Simula la conversión de divisas con el tipo de cambio registrado en una fecha determinada", authorizations = {@Authorization(value = "apiKey") })
	@GetMapping(value = "/tipo_cambio/simulador")	
	public ResponseEntity<?> simladorTipoCambio(
			@RequestParam(value = "fecha", required = false, defaultValue = "") String fecha,
			@RequestParam(value = "idDivisaOrigen", required = false, defaultValue = "-1") Integer idDivisaOrigen,
			@RequestParam(value = "idDivisaDestino", required = false, defaultValue = "-1") Integer idDivisaDestino,
			@RequestParam(value = "monto", required = false) BigDecimal monto,
			HttpServletRequest request) {
		HashMap<String, Object> result = new HashMap<>();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Integer idUser = 0;
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			JwtUser userDetails = (JwtUser) auth.getPrincipal();
			idUser = userDetails.getId();

		} else {
			return new ResponseEntity<>(new RestException("No autorizado"), HttpStatus.UNAUTHORIZED);
		}
		
		
		List<TipoCambio> data = tipoCambioService.findTCByFecha(fecha, idDivisaOrigen, idDivisaDestino);
		if(data.isEmpty()) {
			result.put("success", false);
			result.put("message", "No existe un Tipo de Cambio para la fecha: " + fecha.toString());
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		
		BigDecimal montoDestino = new BigDecimal(0);
		if(data.get(0).getFormula().equals("m/tc")) {
			montoDestino = monto.divide(data.get(0).getTasaCambio(), 2, RoundingMode.HALF_UP); 
		}
		if(data.get(0).getFormula().equals("m*tc")) {
			montoDestino = monto.multiply(data.get(0).getTasaCambio()); 
		}
		HashMap<String, Object> dataRst = new HashMap<>();
		dataRst.put("fecha", fecha);
		dataRst.put("monto_conversion", montoDestino);
		dataRst.put("tasa_cambio", data.get(0).getTasaCambio());
		dataRst.put("divisaOrigen", data.get(0).getDivisaOrigen());
		dataRst.put("divisaDestino", data.get(0).getDivisaDestino());
		
		
		result.put("success", true);
		result.put("message", "Se ha realizado la simulación correctamente.");
		result.put("result", dataRst);
		return new ResponseEntity<>(result, HttpStatus.OK);
		
		
	}
	
	@ApiOperation(value = "Obtiene datos de un Tipo de Cambio Vigente por Fecha y Divisas", authorizations = { @Authorization(value = "apiKey")})
	@GetMapping(value = "/tipo_cambio/vigente/{fecha}/{idDivisaOrigen}/{idDivisaDestino}")
	public ResponseEntity<?> findTipoCambioVigente(
			@PathVariable(value = "fecha") String fecha,
			@PathVariable(value = "idDivisaOrigen") Integer idDivisaOrigen,
			@PathVariable(value = "idDivisaDestino") Integer idDivisaDestino,
			HttpServletRequest request) {
		HashMap<String, Object> result = new HashMap<>();
		List<TipoCambio> data = tipoCambioService.findVigentes(fecha, idDivisaOrigen, idDivisaDestino);
		if(data.isEmpty()) {
			result.put("success", false);
			result.put("message", "No existe un Tipo de Cambio vigente para fecha: " + fecha.toString());
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		
		result.put("success", true);
		result.put("message", "Se ha encontrado el registro.");
		result.put("result", data.get(0));
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Obtiene datos de un Tipo de Cambio por Fecha y Divisas", authorizations = { @Authorization(value = "apiKey")})
	@GetMapping(value = "/tipo_cambio/{fecha}/{idDivisaOrigen}/{idDivisaDestino}")
	public ResponseEntity<?> findTipoCambio(
			@PathVariable(value = "fecha") String fecha,
			@PathVariable(value = "idDivisaOrigen") Integer idDivisaOrigen,
			@PathVariable(value = "idDivisaDestino") Integer idDivisaDestino,
			HttpServletRequest request) {
		HashMap<String, Object> result = new HashMap<>();
		List<TipoCambio> data = tipoCambioService.findTCByFecha(fecha, idDivisaOrigen, idDivisaDestino);
		if(data.isEmpty()) {
			result.put("success", false);
			result.put("message", "No existe un Tipo de Cambio para la fecha: " + fecha.toString());
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		
		result.put("success", true);
		result.put("message", "Se ha encontrado el registro.");
		result.put("result", data.get(0));
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
		
		if(tipoCambio.getDivisaOrigen().getIdDivisa() == tipoCambio.getDivisaDestino().getIdDivisa()) {
			result.put("success", false);
			result.put("message", "Debe seleccionar divisas diferentes para registrar el tipo de cambio.");
			return new ResponseEntity<>(result, HttpStatus.CONFLICT); 
		}
		
		List<TipoCambio> existe = tipoCambioService.findTCByFecha(tipoCambio.getFecha().toString(), tipoCambio.getDivisaOrigen().getIdDivisa(), tipoCambio.getDivisaDestino().getIdDivisa());
		if(!existe.isEmpty()) {
			result.put("success", false);
			result.put("message", "Ya existe un registro vigente del tipo de cambio con la fecha asignada. Le recomendamos editar el registro.");
			return new ResponseEntity<>(result, HttpStatus.CONFLICT); 
		}
		
		try {
			tipoCambio.setCrAt(data.getCrAt());
			tipoCambio.setUserCr(data.getUserCr());
			tipoCambio.setUpAt(new java.sql.Timestamp(System.currentTimeMillis()));
			tipoCambio.setUserUp(idUser);
			tipoCambio.setDtAt(data.getDtAt());
			tipoCambio.setUserDt(data.getUserDt());
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
		cliente.setCrAt(new java.sql.Timestamp(System.currentTimeMillis()));
		cliente.setUserCr(idUser);
		cliente.setUpAt(null);
		cliente.setDtAt(null);
		cliente.setUserUp(null);
		cliente.setUserDt(null);
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
			cliente.setCrAt(data.getCrAt());
			cliente.setUserCr(data.getUserCr());
			cliente.setUpAt(new java.sql.Timestamp(System.currentTimeMillis()));
			cliente.setUserUp(idUser);
			cliente.setDtAt(data.getDtAt());
			cliente.setUserDt(data.getUserDt());
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
