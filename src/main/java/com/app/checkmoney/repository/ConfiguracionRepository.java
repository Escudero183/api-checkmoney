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

import com.app.checkmoney.model.Configuracion;

/**
 * @author Linygn Escudero
 *
 */
@Repository
public interface ConfiguracionRepository extends JpaRepository<Configuracion, Integer> {	
	
	@Query("select s from Configuracion s Where lower(s.llave) like :query")
	public Page<Configuracion> findAllParams(String query, Pageable pageable);
	
	@Query("select s from Configuracion s Where lower(s.llave) like :query")
	public List<Configuracion> findAll(String query);
	
	@Query("select s from Configuracion s Where lower(s.llave) like :query")
	public List<Configuracion> findAll(String query, Sort sort);

}
