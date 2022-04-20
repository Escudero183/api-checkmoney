package com.app.checkmoney.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.app.checkmoney.model.Token;




public interface TokenRepository extends CrudRepository<Token, Integer> {

	@Query("select t from Token t where t.usuario.idUsuario=:idUsuario and t.hash=:token and t.nombreEquipo=:userAgent")
	Token findByHashAndUsuario(String token, Integer idUsuario, String userAgent);

	@Modifying
	@Query("delete from Token t where t.usuario.idUsuario=:idUsuario")
	void deleteByUsuario(Integer idUsuario);

}
