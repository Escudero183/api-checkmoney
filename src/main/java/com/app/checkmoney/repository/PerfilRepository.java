/**
 * 
 */
package com.app.checkmoney.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.checkmoney.model.Perfil;

/**
 * @author Linygn Escudero
 *
 */
@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {
	
	@Query("select p from Perfil p Where p.estado = true and upper(p.nombre) like :query and (:idSistema = -1 or (:idSistema != -1 and p.sistema.idSistema = :idSistema))")
	public Page<Perfil> findAllParams(Integer idSistema, String query, Pageable pageable);
	
	@Query("select p from Perfil p Where p.estado = true and upper(p.nombre) like :query")
	public List<Perfil> findAll(String query);
	
	@Query("select p from Perfil p Where p.estado = true and upper(p.nombre) like :query and (:idSistema = -1 or (:idSistema != -1 and p.sistema.idSistema = :idSistema))")
	public List<Perfil> findAll(Integer idSistema, String query, Sort sort);

}
