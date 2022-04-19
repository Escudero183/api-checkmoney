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

import com.app.checkmoney.model.TipoDocumento;

/**
 * @author Linygn Escudero
 *
 */
@Repository
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Integer> {
	
	@Query("select tp from TipoDocumento tp Where tp.estado = true and (lower(tp.nombre) like :query or lower(tp.abreviatura) like :query)")
	public Page<TipoDocumento> findAllParams(String query, Pageable pageable);
	
	@Query("select tp from TipoDocumento tp Where tp.estado = true and (lower(tp.nombre) like :query or lower(tp.abreviatura) like :query)")
	public List<TipoDocumento> findAll(String query);
	
	@Query("select tp from TipoDocumento tp Where tp.estado = true and (lower(tp.nombre) like :query or lower(tp.abreviatura) like :query)")
	public List<TipoDocumento> findAll(String query, Sort sort);
	
}
