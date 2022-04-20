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

import com.app.checkmoney.model.Transaccion;

/**
 * @author Linygn Escudero
 *
 */
@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Integer>{
	
	@Query("select t1 from Transaccion t1 Where t1.estado = true and (:fechaOperacion = '' or (:fechaOperacion != '' and date(t1.fechaOperacion) = date(:fechaOperacion))) and (lower(t1.cliente.razonSocial) like :query or lower(t1.cliente.nombre) like :query or lower(t1.cliente.apellidoPaterno) like :query or lower(t1.cliente.apellidoMaterno) like :query or lower(t1.cliente.nroDocumento) like :query)")
	public Page<Transaccion> findAllParams(String fechaOperacion, String query, Pageable pageable);
	
	@Query("select t1 from Transaccion t1 Where t1.estado = true and (lower(t1.cliente.razonSocial) like :query or lower(t1.cliente.nombre) like :query or lower(t1.cliente.apellidoPaterno) like :query or lower(t1.cliente.apellidoMaterno) like :query or lower(t1.cliente.nroDocumento) like :query)")
	public List<Transaccion> findAll(String query);
	
	@Query("select t1 from Transaccion t1 Where t1.estado = true and (lower(t1.cliente.razonSocial) like :query or lower(t1.cliente.nombre) like :query or lower(t1.cliente.apellidoPaterno) like :query or lower(t1.cliente.apellidoMaterno) like :query or lower(t1.cliente.nroDocumento) like :query)")
	public List<Transaccion> findAll(String query, Sort sort);
	
}
