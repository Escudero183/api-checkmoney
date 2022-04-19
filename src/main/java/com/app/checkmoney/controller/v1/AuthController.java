package com.app.checkmoney.controller.v1;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.checkmoney.config.exception.ApiCheckMoneyException;
import com.app.checkmoney.config.exception.RestException;
import com.app.checkmoney.config.security.model.JwtAuthenticationRequest;
import com.app.checkmoney.config.security.model.JwtUser;
import com.app.checkmoney.config.security.token.JwtTokenUtil;
import com.app.checkmoney.model.Token;
import com.app.checkmoney.model.Usuario;
import com.app.checkmoney.service.TokenService;
import com.app.checkmoney.service.UsuarioService;
import com.app.checkmoney.service.UsuarioSistemaService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiResponses(value = { @ApiResponse(code = 200, message = "La solicitud ha tenido éxito"),
		@ApiResponse(code = 401, message = "El servidor no ha encontrado nada que coincida con el URI de solicitud") })
@Api(description = "Microservicio de autentificación")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private UserDetailsService jwtUserDetailService;
	@Autowired
	private TokenService tokenService;	
	@Autowired
	private UsuarioSistemaService usuarioSistemaService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	/*
	 * @Autowired private TipoRecursoService tipoRecursoService;
	 * 
	 * @Autowired private CorreoService correoService;
	 * 
	 * @Autowired private ConfiguracionGeneralService configuracionGeneralService;
	 */

	@CrossOrigin(origins = "*")
	@PostMapping("/auth")
	@ApiOperation(value = "CREA EL TOKEN, NO ES NECESARIO ESTAR AUTENTIFICADO")
	public ResponseEntity<?> createToken(@RequestBody JwtAuthenticationRequest authenticationRequest,
			HttpServletRequest request) {
		// verificamos la autentificación de nuestra configuración
		try {
			authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
			
			// obtenemos el detalle del usuario
			UserDetails userDetails = jwtUserDetailService.loadUserByUsername(authenticationRequest.getUsername());

			// creamos la variable que sea nuestro token
			String token;
			if (checkUsuario(((JwtUser) userDetails).getId())) {
				JwtUser jwtUser = (JwtUser) userDetails;
				// definimos el tiempo de expiración de nuestro token 1 dia laborable 8 horas
				jwtTokenUtil.setExpiration(28800 * 1000L);

				// obtenemos el token con los datos pasados
				String userAgent = request.getHeader("User-Agent");
				token = jwtTokenUtil.generateToken(userDetails, userAgent);

				Usuario usuarioBean = usuarioService.findByLogin(jwtUser.getUsername().toUpperCase());
				try {
					InetAddress remoteInetAddress = InetAddress.getByName(request.getRemoteAddr());
					Token tokenBean = new Token();
					tokenBean.setHostname(remoteInetAddress.getHostAddress());
					tokenBean.setNombreEquipo(userAgent);
					tokenBean.setUsuario(usuarioBean);
					tokenBean.setHash(token);
					
					tokenService.insert(tokenBean);
				} catch (UnknownHostException e) {

				}
				
				HashMap<String, Object> result = new HashMap<String, Object>();
				result.put("tokenType", "Bearer");
				result.put("user", jwtUser);
				result.put("token", token);
				result.put("sistemas", usuarioSistemaService.findByUsuario(usuarioBean.getIdUsuario()));
				
				return new ResponseEntity<>(result, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new RestException("El usuario no tiene acceso"), HttpStatus.NOT_FOUND);
			}
		} catch (ApiCheckMoneyException | UnsupportedEncodingException e) {
			return new ResponseEntity<>(new RestException(e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value = "DESTRUYE EL TOKEN", authorizations = { @Authorization(value = "apiKey") })
	@DeleteMapping("/logout")
	public ResponseEntity<?> destroyToken(HttpServletRequest request) {
		String requestHeader = request.getHeader("Authorization");
		String token = requestHeader.substring(7);
		try {
			String username = jwtTokenUtil.getUsernameFromToken(token);
			Usuario usuario = usuarioService.findByLogin(username);
			Token tokenBean = tokenService.findTokenByUsuario(token, usuario.getIdUsuario(),
					request.getHeader("User-Agent"));
			tokenService.delete(tokenBean);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException
				| IllegalArgumentException | UnsupportedEncodingException e) {
			return new ResponseEntity<>(new RestException(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "VERIFICA EL TOKEN", authorizations = { @Authorization(value = "apiKey") })
	@GetMapping("/check")
	public ResponseEntity<?> checkToken(HttpServletRequest request) {
		String requestHeader = request.getHeader("Authorization");
		String token = requestHeader.substring(7);
		HashMap<String, Object> result = new HashMap<String, Object>();
		try {
			String username = jwtTokenUtil.getUsernameFromToken(token);
			result.put("tokenValid", true);
			result.put("tokenUser", username);
			result.put("tokenExpired", new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a")
					.format(jwtTokenUtil.getExpirationDateFromToken(token)));

		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException
				| IllegalArgumentException | UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
			result.put("token_valid", false);
			result.put("token_user", "");
			result.put("token_expired", "");
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/*
	 * @CrossOrigin(origins = "*")
	 * 
	 * @PutMapping(value = "/playerid")
	 * 
	 * @ApiOperation(value = "ACTUALIZA ONE SIGNAL ID POR USUARIO", authorizations =
	 * { @Authorization(value = "apiKey") }) public ResponseEntity<?>
	 * playerid(@RequestParam(value = "playerid", required = false, defaultValue =
	 * "") String playerid, HttpServletResponse response, HttpServletRequest
	 * request) throws IOException { Authentication auth =
	 * SecurityContextHolder.getContext().getAuthentication(); Integer idUser = 0;
	 * 
	 * if (!(auth instanceof AnonymousAuthenticationToken)) { JwtUser userDetails =
	 * (JwtUser) auth.getPrincipal(); idUser = userDetails.getId(); } else { return
	 * new ResponseEntity<>(new RestException("no autorizado"),
	 * HttpStatus.UNAUTHORIZED); }
	 * 
	 * usuarioService.updateIdPlayer(idUser, playerid);
	 * 
	 * return new ResponseEntity<>(playerid, HttpStatus.OK); }
	 */

	/*
	 * @ApiOperation(value = "OBTIENE INFORMACIÓN DEL USUARIO", authorizations =
	 * { @Authorization(value = "apiKey") })
	 * 
	 * @GetMapping("/info") public ResponseEntity<?> findInfo(HttpServletRequest
	 * request) {
	 * 
	 * String requestHeader = request.getHeader("Authorization"); String authToken =
	 * requestHeader.substring(7); String login; try {
	 * 
	 * login = jwtTokenUtil.getUsernameFromToken(authToken); UserDetails userDetails
	 * = jwtUserDetailService.loadUserByUsername(login); JwtUser jwtUser = (JwtUser)
	 * userDetails;
	 * 
	 * Usuario usuario = usuarioService.findByLogin(login);
	 * 
	 * // aqui evaluamos si tiene un contrato o mejor dicho si pertenece a una
	 * unidad // orgánica
	 * 
	 * // aqui obtenemos los accesos categorizado por tipo de recurso
	 * HashMap<String, Object> accesos =
	 * tipoRecursoService.findAllMenuByUsuario(usuario.getIdUsuario());
	 * HashMap<String, Object> result = new HashMap<>(); result.put("user",
	 * jwtUser); result.put("accesoMenus", accesos.get("acceso_menus"));
	 * result.put("accesoRecursos", accesos.get("acceso_recursos")); return new
	 * ResponseEntity<>(result, HttpStatus.OK); } catch (ExpiredJwtException |
	 * UnsupportedJwtException | MalformedJwtException | SignatureException |
	 * IllegalArgumentException | UnsupportedEncodingException e) { return new
	 * ResponseEntity<>(new RestException(e.getMessage()), HttpStatus.NOT_FOUND); }
	 * }
	 */

	private void authenticate(String username, String password) throws ApiCheckMoneyException {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);
		// le pasamos los datos del formulario
		try {
			
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new ApiCheckMoneyException("User is disabled!");

		} catch (BadCredentialsException e) {
			throw new ApiCheckMoneyException("Datos incorrectos!");
		}
	}

	private boolean checkUsuario(Integer idUsuario) {
		Usuario usuario = usuarioService.findById(idUsuario);
		if (usuario == null) {
			return false;
		} else {
			return usuario.isEstado();
		}
	}
}
