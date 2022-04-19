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

import com.app.checkmoney.model.TipoCambio;

/**
 * @author Linygn Escudero
 *
 */
@Repository
public interface TipoCambioRepository extends JpaRepository<TipoCambio, Integer>{
	
	@Query("select t1 from TipoCambio t1 Where t1.estado = true and t1.vigente=:vigente and (lower(t1.descripcion) like :query) and (:fecha = '' or (:fecha != '' and date(t1.fecha) = date(:fecha)))")
	public Page<TipoCambio> findAllParams(String fecha, Boolean vigente, String query, Pageable pageable);
	
	@Query("select t1 from TipoCambio t1 Where t1.estado = true and (lower(t1.descripcion) like :query)")
	public List<TipoCambio> findAll(String query);
	
	@Query("select t1 from TipoCambio t1 Where t1.estado = true and (lower(t1.descripcion) like :query)")
	public List<TipoCambio> findAll(String query, Sort sort);

}
