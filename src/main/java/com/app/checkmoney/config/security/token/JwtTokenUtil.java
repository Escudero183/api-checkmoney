package com.app.checkmoney.config.security.token;



import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.app.checkmoney.config.security.model.JwtUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.DefaultClock;


@Component
public class JwtTokenUtil {

	private final Clock clock = DefaultClock.INSTANCE;

	static final String CLAIM_KEY_USERNAME = "sub";
	static final String CLAIM_KEY_CREATED = "iat";

	private final String secret = "$Linygn183$*#";

	// Tiempos de expiración por defecto (7 días 60*60*24*7)
	private Long expiration = 604800L;

	/**
	 * Setea la fecha de expiracion
	 * 
	 * @param expiration
	 */
	public void setExpiration(Long expiration) {
		this.expiration = expiration;
	}

	/**
	 * Obtiene el usuario a partir del token
	 *
	 * @param token
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IllegalArgumentException
	 * @throws SignatureException
	 * @throws MalformedJwtException
	 * @throws UnsupportedJwtException
	 * @throws ExpiredJwtException
	 */
	public String getUsernameFromToken(String token) throws ExpiredJwtException, UnsupportedJwtException,
			MalformedJwtException, SignatureException, IllegalArgumentException, UnsupportedEncodingException {
		return getClaimFromToken(token, Claims::getSubject);
	}

	/**
	 * función que devuelve parametros del token
	 * 
	 * @param <T>
	 * @param token
	 * @param claimsResolver
	 * @return
	 * @throws ExpiredJwtException
	 * @throws UnsupportedJwtException
	 * @throws MalformedJwtException
	 * @throws SignatureException
	 * @throws IllegalArgumentException
	 * @throws UnsupportedEncodingException
	 */
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver)
			throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException,
			IllegalArgumentException, UnsupportedEncodingException {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	/**
	 * función que genera el token
	 * 
	 * @param userDetails
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String generateToken(UserDetails userDetails, String userAgent) throws UnsupportedEncodingException {
		Map<String, Object> claims = new HashMap<>();
		claims.put("userAgent", userAgent);
		return doGenerateToken(claims, userDetails.getUsername());
	}

	/**
	 * Función que devuelve la fecha de expiración del token
	 * 
	 * @param token
	 * @return
	 * @throws ExpiredJwtException
	 * @throws UnsupportedJwtException
	 * @throws MalformedJwtException
	 * @throws SignatureException
	 * @throws IllegalArgumentException
	 * @throws UnsupportedEncodingException
	 */
	public Date getExpirationDateFromToken(String token) throws ExpiredJwtException, UnsupportedJwtException,
			MalformedJwtException, SignatureException, IllegalArgumentException, UnsupportedEncodingException {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	/**
	 * Función que devuelve la fecha de emisión del token
	 * 
	 * @param token
	 * @return
	 * @throws ExpiredJwtException
	 * @throws UnsupportedJwtException
	 * @throws MalformedJwtException
	 * @throws SignatureException
	 * @throws IllegalArgumentException
	 * @throws UnsupportedEncodingException
	 */
	public Date getIssuedAtDateFromToken(String token) throws ExpiredJwtException, UnsupportedJwtException,
			MalformedJwtException, SignatureException, IllegalArgumentException, UnsupportedEncodingException {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	/**
	 * Función que valida el token
	 * 
	 * @param token
	 * @param userDetails
	 * @return
	 * @throws ExpiredJwtException
	 * @throws UnsupportedJwtException
	 * @throws MalformedJwtException
	 * @throws SignatureException
	 * @throws IllegalArgumentException
	 * @throws UnsupportedEncodingException
	 */
	public Boolean validateToken(String token, UserDetails userDetails)
			throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException,
			IllegalArgumentException, UnsupportedEncodingException {
		JwtUser user = (JwtUser) userDetails;
		// obtenemos el usuario del token
		final String username = getUsernameFromToken(token);
		// final Date created = getIssuedAtDateFromToken(token);
		// final Date expiration = getExpirationDateFromToken(token);
		return (username.equals(user.getUsername()) && !isTokenExpired(token));
	}

//	public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset)
//			throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException,
//			IllegalArgumentException, UnsupportedEncodingException {
//		final Date created = getIssuedAtDateFromToken(token);
//		return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset) && (!isTokenExpired(token));
//	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// Funciones de apoyo
	//////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Función que verifica que si la fecha de creación del token es antes de la
	 * actualización de la contraseña
	 * 
	 * @param created
	 * @param lastPasswordReset
	 * @return
	 */
//	private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
//		return (lastPasswordReset != null && created.before(lastPasswordReset));
//	}

	/**
	 * Función que verifica que el token no haya sido expirado
	 * 
	 * @param token
	 * @return
	 * @throws ExpiredJwtException
	 * @throws UnsupportedJwtException
	 * @throws MalformedJwtException
	 * @throws SignatureException
	 * @throws IllegalArgumentException
	 * @throws UnsupportedEncodingException
	 */
	private Boolean isTokenExpired(String token) throws ExpiredJwtException, UnsupportedJwtException,
			MalformedJwtException, SignatureException, IllegalArgumentException, UnsupportedEncodingException {
		final Date fExpiration = getExpirationDateFromToken(token);
		return fExpiration.before(clock.now());
	}

	/**
	 * Función que genera el token
	 * 
	 * @param claims
	 * @param username
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String doGenerateToken(Map<String, Object> claims, String username) throws UnsupportedEncodingException {
		Date createdDate = clock.now();
		Date expirationDate = calculeEspirationDate(createdDate);
		return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(createdDate)
				.setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, this.secret.getBytes("UTF-8"))
				.compact();
	}

	/**
	 * Función que calcula la fecha de expiración
	 * 
	 * @param createdDate
	 * @return
	 */
	private Date calculeEspirationDate(Date createdDate) {
		return new Date(createdDate.getTime() + expiration * 1000);
	}

	/**
	 * Función que obtiene todo los
	 * 
	 * @param token
	 * @return
	 * @throws ExpiredJwtException
	 * @throws UnsupportedJwtException
	 * @throws MalformedJwtException
	 * @throws SignatureException
	 * @throws IllegalArgumentException
	 * @throws UnsupportedEncodingException
	 */
	private Claims getAllClaimsFromToken(String token) throws ExpiredJwtException, UnsupportedJwtException,
			MalformedJwtException, SignatureException, IllegalArgumentException, UnsupportedEncodingException {
		return Jwts.parser().setSigningKey(this.secret.getBytes("UTF-8")).parseClaimsJws(token).getBody();
	}

}
