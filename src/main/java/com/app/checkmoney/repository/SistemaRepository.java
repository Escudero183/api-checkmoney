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

import com.app.checkmoney.model.Sistema;

/**
 * @author Linygn Escudero
 *
 */
@Repository
public interface SistemaRepository extends JpaRepository<Sistema, Integer>{
	
	@Query("select s from Sistema s Where s.estado = true and lower(s.nombre) like :query")
	public Page<Sistema> findAllParams(String query, Pageable pageable);
	
	@Query("select s from Sistema s Where s.estado = true and lower(s.nombre) like :query")
	public List<Sistema> findAll(String query);
	
	@Query("select s from Sistema s Where s.estado = true and lower(s.nombre) like :query")
	public List<Sistema> findAll(String query, Sort sort);

}
