package com.app.checkmoney.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.app.checkmoney.model.Usuario;




public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	@Query("select u from Usuario u Where upper(u.login)=:nickname and u.estado = true")
	public Usuario findByLogin(String nickname); 

	@Query("select u from Usuario u Where u.estado = true and upper(u.login) like :query")
	public List<Usuario> findAll(String query, Sort sort);

	@Query("select u from Usuario u Where u.estado = true and (lower(u.login) like :query)")
	public Page<Usuario> findAllParams(String query, Pageable pageable);

	@Query("select u from Usuario u where u.estado = true")
	public List<Usuario> findAll(String query);
	
	@Query("select u from Usuario u where u.estado = true and idUsuario in(:usuarios)")
	public List<Usuario> findAll(List<Integer> usuarios);
	
	@Modifying
	@Query("update Usuario u set u.password=:password Where u.estado = true and u.idUsuario=:id")
	public void resetPassword(Integer id, String password);
	
}
