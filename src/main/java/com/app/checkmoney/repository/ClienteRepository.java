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

import com.app.checkmoney.model.Cliente;

/**
 * @author Linygn Escudero
 *
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer>{
	
	@Query("select c from Cliente c Where c.estado = true and (lower(c.razonSocial) like :query or lower(c.nombre) like :query or lower(c.apellidoPaterno) like :query or lower(c.apellidoMaterno) like :query or lower(c.nroDocumento) like :query)")
	public Page<Cliente> findAllParams(String query, Pageable pageable);
	
	@Query("select c from Cliente c Where c.estado = true and (lower(c.razonSocial) like :query or lower(c.nombre) like :query or lower(c.apellidoPaterno) like :query or lower(c.apellidoMaterno) like :query or lower(c.nroDocumento) like :query)")
	public List<Cliente> findAll(String query);
	
	@Query("select c from Cliente c Where c.estado = true and (lower(c.razonSocial) like :query or lower(c.nombre) like :query or lower(c.apellidoPaterno) like :query or lower(c.apellidoMaterno) like :query or lower(c.nroDocumento) like :query)")
	public List<Cliente> findAll(String query, Sort sort);
	
	
	@Query("select count(c) from Cliente c Where c.estado = true")
	public Integer getTotal();

}
