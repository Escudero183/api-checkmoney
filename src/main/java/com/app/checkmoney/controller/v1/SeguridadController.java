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
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.app.checkmoney.model.Modulo;
import com.app.checkmoney.model.Perfil;
import com.app.checkmoney.model.Sistema;
import com.app.checkmoney.model.Usuario;
import com.app.checkmoney.service.ModuloService;
import com.app.checkmoney.service.PerfilService;
import com.app.checkmoney.service.SistemaService;
import com.app.checkmoney.service.UsuarioService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

/**
 * @author Linygn Escudero
 *
 */
@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api/v1/seguridad", produces = { MediaType.APPLICATION_JSON_VALUE })
public class SeguridadController {
	
	@Autowired
	private PerfilService perfilService;
	
	@Autowired
	private SistemaService sistemaService;
	
	@Autowired
	private ModuloService moduloService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/* Servicios para la Entidad Perfil */
	@ApiOperation(value = "Crea un Perfil", authorizations = {@Authorization(value = "apiKey") })	
	@PostMapping(value = "/perfil")
	public ResponseEntity<?> savePerfil(@RequestBody Perfil perfil, HttpServletRequest request){
		HashMap<String, Object> result = new HashMap<>();
		perfil.setEstado(true);
		Perfil data = perfilService.insert(perfil);
		
		
		result.put("success", true);
		result.put("message", "Se ha registrado correctamente.");
		result.put("result", data);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Lista todos los Perfiles", authorizations = {@Authorization(value = "apiKey") })
	@GetMapping(value = "/perfil")	
	public ResponseEntity<?> findAllPerfil(
			@RequestParam(value = "idSistema", required = false, defaultValue = "-1") Integer idSistema,
			@RequestParam(value = "query", required = false, defaultValue = "") String query,
			@RequestParam(value = "page", required = false, defaultValue = "-1") int page,
			@RequestParam(value = "limit", required = false, defaultValue = "-1") int limit,
			@RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy,
			HttpServletRequest request) {
		
		int maxPage = 10;
		
		if (page == -1 && limit == -1 && "".equals(sortBy)) {
			page = 1;
			limit = maxPage;		
		}else if (limit != -1 && page == -1) {
			page = 1;
		} else if (page != -1 && limit == -1) {
			limit = maxPage;
		}
		
		return new ResponseEntity<>(perfilService.findAll(idSistema, query, page, limit, sortBy), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Obtiene datos de un Perfil", authorizations = { @Authorization(value = "apiKey")})
	@GetMapping(value = "/perfil/{idPerfil}")
	public ResponseEntity<?> findPerfil(@PathVariable(value = "idPerfil") Integer idPerfil, HttpServletRequest request) {
		HashMap<String, Object> result = new HashMap<>();
		Perfil data = perfilService.findById(idPerfil);
		if(data == null) {
			result.put("success", false);
			result.put("message", "No existe el Perfil con código: " + idPerfil);
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		
		result.put("success", true);
		result.put("message", "Se ha encontrado el registro.");
		result.put("result", data);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Actualiza un Perfil", authorizations = { @Authorization(value = "apiKey")})
	@PutMapping(value = "/perfil")
	public ResponseEntity<?> updatePerfil (@RequestBody Perfil perfil, HttpServletRequest request) {
		HashMap<String, Object> result = new HashMap<>();
		Perfil data = perfilService.findById(perfil.getIdPerfil());
		if(data == null) {
			result.put("success", false);
			result.put("message", "No existe el Perfil con código: " + perfil.getIdPerfil());
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		try {
			perfil.setEstado(true);
			perfilService.update(perfil);
			result.put("success", true);
			result.put("message", "Se ha actualizado los datos del registro.");
			result.put("result", perfil);
			return new ResponseEntity<>(result, HttpStatus.OK);
			
		} catch (Exception ex) {
			return new ResponseEntity<>(new ApiCheckMoneyException(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}			
	}
	
	@ApiOperation(value = "Elimina un Perfil", authorizations = { @Authorization(value = "apiKey")})
	@DeleteMapping(value = "/perfil/{idPerfil}")
	public ResponseEntity<?> deletePerfil (@PathVariable(value = "idPerfil") Integer idPerfil, HttpServletRequest request){
		HashMap<String, Object> result = new HashMap<>();
		Perfil data = perfilService.findById(idPerfil);
		if(data == null) {
			result.put("success", false);
			result.put("message", "No existe el Perfil con código: " + idPerfil);
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		try {
			data.setEstado(false);
			perfilService.delete(data);
			result.put("success", true);
			result.put("message", "Se ha eliminado los datos del registro.");
			result.put("result", data);
			return new ResponseEntity<>(result, HttpStatus.OK);
			
		} catch (Exception ex) {
			return new ResponseEntity<>(new ApiCheckMoneyException(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}	
	/* Fin Servicios Entidad Perfil */
	
	/* Servicios para la Entidad Sistema */
	@ApiOperation(value = "Crea un Sistema", authorizations = {@Authorization(value = "apiKey") })	
	@PostMapping(value = "/sistema")
	public ResponseEntity<?> saveSistema(@RequestBody Sistema sistema, HttpServletRequest request){
		HashMap<String, Object> result = new HashMap<>();
		sistema.setEstado(true);
		Sistema data = sistemaService.insert(sistema);
				
		result.put("success", true);
		result.put("message", "Se ha registrado correctamente.");
		result.put("result", data);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Lista todos los Sistemas", authorizations = {@Authorization(value = "apiKey") })
	@GetMapping(value = "/sistema")	
	public ResponseEntity<?> findAllSistema(
			@RequestParam(value = "query", required = false, defaultValue = "") String query,
			@RequestParam(value = "page", required = false, defaultValue = "-1") int page,
			@RequestParam(value = "limit", required = false, defaultValue = "-1") int limit,
			@RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy,
			HttpServletRequest request) {
		
		int maxPage = 10;
		
		if (page == -1 && limit == -1 && "".equals(sortBy)) {
			page = 1;
			limit = maxPage;		
		}else if (limit != -1 && page == -1) {
			page = 1;
		} else if (page != -1 && limit == -1) {
			limit = maxPage;
		}
		
		return new ResponseEntity<>(sistemaService.findAll(query, page, limit, sortBy), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Obtiene datos de un Sistema", authorizations = { @Authorization(value = "apiKey")})
	@GetMapping(value = "/sistema/{idSistema}")
	public ResponseEntity<?> findSistema(@PathVariable(value = "idSistema") Integer idSistema, HttpServletRequest request) {
		HashMap<String, Object> result = new HashMap<>();
		Sistema data = sistemaService.findById(idSistema);
		if(data == null) {
			result.put("success", false);
			result.put("message", "No existe el Sistema con código: " + idSistema);
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		
		result.put("success", true);
		result.put("message", "Se ha encontrado el registro.");
		result.put("result", data);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Actualiza un Sistema", authorizations = { @Authorization(value = "apiKey")})
	@PutMapping(value = "/sistema")
	public ResponseEntity<?> updateSistema (@RequestBody Sistema sistema, HttpServletRequest request) {
		HashMap<String, Object> result = new HashMap<>();
		Sistema data = sistemaService.findById(sistema.getIdSistema());
		if(data == null) {
			result.put("success", false);
			result.put("message", "No existe el Sistema con código: " + sistema.getIdSistema());
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		try {
			sistema.setEstado(true);
			sistemaService.update(sistema);
			result.put("success", true);
			result.put("message", "Se ha actualizado los datos del registro.");
			result.put("result", sistema);
			return new ResponseEntity<>(result, HttpStatus.OK);
			
		} catch (Exception ex) {
			return new ResponseEntity<>(new ApiCheckMoneyException(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}			
	}
	
	@ApiOperation(value = "Elimina un Sistema", authorizations = { @Authorization(value = "apiKey")})
	@DeleteMapping(value = "/sistema/{idSistema}")
	public ResponseEntity<?> deleteSistema (@PathVariable(value = "idSistema") Integer idSistema, HttpServletRequest request){
		HashMap<String, Object> result = new HashMap<>();
		Sistema data = sistemaService.findById(idSistema);
		if(data == null) {
			result.put("success", false);
			result.put("message", "No existe el Sistema con código: " + idSistema);
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		try {
			data.setEstado(false);
			sistemaService.delete(data);
			result.put("success", true);
			result.put("message", "Se ha eliminado los datos del registro.");
			result.put("result", data);
			return new ResponseEntity<>(result, HttpStatus.OK);
			
		} catch (Exception ex) {
			return new ResponseEntity<>(new ApiCheckMoneyException(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}	
	/* Fin Servicios Entidad Sistema */
	
	/* Servicios para la Entidad Modulo */
	@ApiOperation(value = "Crea un Modulo", authorizations = {@Authorization(value = "apiKey") })	
	@PostMapping(value = "/modulo")
	public ResponseEntity<?> saveModulo(@RequestBody Modulo modulo, HttpServletRequest request){
		HashMap<String, Object> result = new HashMap<>();
		modulo.setEstado(true);
		Modulo data = moduloService.insert(modulo);
		
		
		result.put("success", true);
		result.put("message", "Se ha registrado correctamente.");
		result.put("result", data);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Lista todos los Modulos de un Sistema", authorizations = {@Authorization(value = "apiKey") })
	@GetMapping(value = "/modulo")	
	public ResponseEntity<?> findAllModulo(
			@RequestParam(value = "tipo", required = false, defaultValue = "grilla") String tipo,
			@RequestParam(value = "idSistema", required = false, defaultValue = "-1") Integer idSistema,
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
			
			return new ResponseEntity<>(moduloService.findAll(idSistema, query, page, limit, sortBy), HttpStatus.OK);
		}else {
			return new ResponseEntity<>(moduloService.findAll(idSistema, query, sortBy), HttpStatus.OK);
		}		
	}
	
	@ApiOperation(value = "Obtiene datos de un Modulo", authorizations = { @Authorization(value = "apiKey")})
	@GetMapping(value = "/modulo/{idModulo}")
	public ResponseEntity<?> findModulo(@PathVariable(value = "idModulo") Integer idModulo, HttpServletRequest request) {
		HashMap<String, Object> result = new HashMap<>();
		Modulo data = moduloService.findById(idModulo);
		if(data == null) {
			result.put("success", false);
			result.put("message", "No existe el Modulo con código: " + idModulo);
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		
		result.put("success", true);
		result.put("message", "Se ha encontrado el registro.");
		result.put("result", data);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Actualiza un Modulo", authorizations = { @Authorization(value = "apiKey")})
	@PutMapping(value = "/modulo")
	public ResponseEntity<?> updateModulo (@RequestBody Modulo modulo, HttpServletRequest request) {
		HashMap<String, Object> result = new HashMap<>();
		Modulo data = moduloService.findById(modulo.getIdModulo());
		if(data == null) {
			result.put("success", false);
			result.put("message", "No existe el Modulo con código: " + modulo.getIdModulo());
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		try {
			modulo.setEstado(true);
			moduloService.update(modulo);
			result.put("success", true);
			result.put("message", "Se ha actualizado los datos del registro.");
			result.put("result", modulo);
			return new ResponseEntity<>(result, HttpStatus.OK);
			
		} catch (Exception ex) {
			return new ResponseEntity<>(new ApiCheckMoneyException(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}			
	}
	
	@ApiOperation(value = "Elimina un Modulo", authorizations = { @Authorization(value = "apiKey")})
	@DeleteMapping(value = "/modulo/{idModulo}")
	public ResponseEntity<?> deleteModulo (@PathVariable(value = "idModulo") Integer idModulo, HttpServletRequest request){
		HashMap<String, Object> result = new HashMap<>();
		Modulo data = moduloService.findById(idModulo);
		if(data == null) {
			result.put("success", false);
			result.put("message", "No existe el Modulo con código: " + idModulo);
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		try {
			data.setEstado(false);
			moduloService.delete(data);
			result.put("success", true);
			result.put("message", "Se ha eliminado los datos del registro.");
			result.put("result", data);
			return new ResponseEntity<>(result, HttpStatus.OK);
			
		} catch (Exception ex) {
			return new ResponseEntity<>(new ApiCheckMoneyException(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}	
	/* Fin Servicios Entidad Modulo */
	
	/* Servicios para la Entidad Usuario */
	@ApiOperation(value = "Crea un Usuario", authorizations = {@Authorization(value = "apiKey") })	
	@PostMapping(value = "/usuario")
	public ResponseEntity<?> saveUsuario(@RequestBody Usuario usuario, HttpServletRequest request){
		HashMap<String, Object> result = new HashMap<>();
		
		if(usuarioService.findByLogin(usuario.getLogin().toUpperCase()) != null) {
			result.put("success", false);
			result.put("message", "Ya existe un registro con el Login.");				
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);				
		}
		usuario.setFechaAlta(new java.sql.Timestamp(System.currentTimeMillis()));
		usuario.setPassword(passwordEncoder.encode(usuario.getLogin()));
		usuario.setEstado(true);
		Usuario data = usuarioService.insert(usuario);
				
		result.put("success", true);
		result.put("message", "Se ha registrado correctamente.");
		result.put("result", data);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Lista todas los Usuarios", authorizations = {@Authorization(value = "apiKey") })
	@GetMapping(value = "/usuario")	
	public ResponseEntity<?> findAllUsuario(
			@RequestParam(value = "query", required = false, defaultValue = "") String query,
			@RequestParam(value = "page", required = false, defaultValue = "-1") int page,
			@RequestParam(value = "limit", required = false, defaultValue = "-1") int limit,
			@RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy,
			HttpServletRequest request) {
		
		int maxPage = 10;
		
		if (page == -1 && limit == -1 && "".equals(sortBy)) {
			page = 1;
			limit = maxPage;		
		}else if (limit != -1 && page == -1) {
			page = 1;
		} else if (page != -1 && limit == -1) {
			limit = maxPage;
		}
		
		return new ResponseEntity<>(usuarioService.findAll(query, page, limit, sortBy), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Obtiene datos de un Usuario", authorizations = { @Authorization(value = "apiKey")})
	@GetMapping(value = "/usuario/{idUsuario}")
	public ResponseEntity<?> findUsuario(@PathVariable(value = "idUsuario") Integer idUsuario, HttpServletRequest request) {
		HashMap<String, Object> result = new HashMap<>();
		Usuario data = usuarioService.findById(idUsuario);
		if(data == null) {
			result.put("success", false);
			result.put("message", "No existe el Usuario con código: " + idUsuario);
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		
		result.put("success", true);
		result.put("message", "Se ha encontrado el registro.");
		result.put("result", data);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Actualiza un Usuario", authorizations = { @Authorization(value = "apiKey")})
	@PutMapping(value = "/usuario")
	public ResponseEntity<?> updateUsuario (@RequestBody Usuario usuario, HttpServletRequest request) {
		HashMap<String, Object> result = new HashMap<>();
		Usuario data = usuarioService.findById(usuario.getIdUsuario());
		if(data == null) {
			result.put("success", false);
			result.put("message", "No existe el Usuario con código: " + usuario.getIdUsuario());
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		try {
			usuario.setEstadoCuenta(data.getEstadoCuenta());
			usuario.setPassword(data.getPassword());
			usuario.setFechaAlta(data.getFechaAlta());
			usuario.setFechaBaja(data.getFechaBaja());
			usuario.setEstado(true);
			usuarioService.update(usuario);
			result.put("success", true);
			result.put("message", "Se ha actualizado los datos del registro.");
			result.put("result", usuario);
			return new ResponseEntity<>(result, HttpStatus.OK);
			
		} catch (Exception ex) {
			return new ResponseEntity<>(new ApiCheckMoneyException(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}			
	}
	
	@ApiOperation(value = "Actualiza Contraseña de un Usuario", authorizations = { @Authorization(value = "apiKey")})
	@PutMapping(value = "/usuario/resetPassword")
	public ResponseEntity<?> updatePasswordUsuario (@RequestBody Usuario usuario, HttpServletRequest request) {
		HashMap<String, Object> result = new HashMap<>();
		Usuario data = usuarioService.findById(usuario.getIdUsuario());
		if(data == null) {
			result.put("success", false);
			result.put("message", "No existe el Usuario con código: " + usuario.getIdUsuario());
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		try {
			usuarioService.resetPassword(usuario.getIdUsuario(), passwordEncoder.encode(usuario.getPassword()));
			result.put("success", true);
			result.put("message", "Se ha actualizado la contraseña del usuario.");
			result.put("result", usuario);
			return new ResponseEntity<>(result, HttpStatus.OK);
			
		} catch (Exception ex) {
			return new ResponseEntity<>(new ApiCheckMoneyException(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}			
	}
	
	@ApiOperation(value = "Elimina un Usuario", authorizations = { @Authorization(value = "apiKey")})
	@DeleteMapping(value = "/usuario/{idUsuario}")
	public ResponseEntity<?> deleteUsuario (@PathVariable(value = "idUsuario") Integer idUsuario, HttpServletRequest request){
		HashMap<String, Object> result = new HashMap<>();
		Usuario data = usuarioService.findById(idUsuario);
		if(data == null) {
			result.put("success", false);
			result.put("message", "No existe el Usuario con código: " + idUsuario);
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
		}
		try {
			data.setEstado(false);
			usuarioService.delete(data);
			result.put("success", true);
			result.put("message", "Se ha eliminado los datos del registro.");
			result.put("result", data);
			return new ResponseEntity<>(result, HttpStatus.OK);
			
		} catch (Exception ex) {
			return new ResponseEntity<>(new ApiCheckMoneyException(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}	
	/* Fin Servicios Entidad Usuario */

}
