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

import com.app.checkmoney.model.Divisa;

/**
 * @author Linygn Escudero
 *
 */
@Repository
public interface DivisaRepository extends JpaRepository<Divisa, Integer>{
	
	@Query("select t1 from Divisa t1 Where t1.estado = true and (lower(t1.nombre) like :query or lower(t1.codigoIso) like :query)")
	public Page<Divisa> findAllParams(String query, Pageable pageable);
	
	@Query("select t1 from Divisa t1 Where t1.estado = true and (lower(t1.nombre) like :query or lower(t1.codigoIso) like :query)")
	public List<Divisa> findAll(String query);
	
	@Query("select t1 from Divisa t1 Where t1.estado = true and (lower(t1.nombre) like :query or lower(t1.codigoIso) like :query)")
	public List<Divisa> findAll(String query, Sort sort);

}
