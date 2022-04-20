/**
 * 
 */
package com.app.checkmoney.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.checkmoney.model.Modulo;

/**
 * @author Linygn Escudero
 *
 */
@Repository
public interface ModuloRepository extends CrudRepository<Modulo, Integer>{
	
	@Query("select m from Modulo m Where m.estado = true and lower(m.nombre) like :query and m.sistema.idSistema = :idSistema")
	public Page<Modulo> findAllParams(Integer idSistema, String query, Pageable pageable);
	
	@Query("select m from Modulo m Where m.estado = true and lower(m.nombre) like :query")
	public List<Modulo> findAll(String query);
	
	@Query("select m from Modulo m Where m.estado = true and lower(m.nombre) like :query and m.sistema.idSistema = :idSistema")
	public List<Modulo> findAll(Integer idSistema, String query, Sort sort);
}
