/**
 * 
 */
package com.app.checkmoney.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.checkmoney.model.IUbigeo;
import com.app.checkmoney.model.Ubigeo;

/**
 * @author Linygn Escudero
 *
 */
@Repository
public interface UbigeoRepository extends JpaRepository<Ubigeo, String>{
	
	@Query("select u from Ubigeo u Where u.idUbigeo like '%0000'")
	public List<Ubigeo> findDptos();
	
	//@Query("select u from Ubigeo u Where u.idUbigeo = :codUbigeo || '%0000'")
	@Query(value = "SELECT "
			+ "u.id_ubigeo, "			
			+ "(select udpto.descripcion From catalogo.ubigeo as udpto Where udpto.id_ubigeo = substring(:codUbigeo,1,2) || '0000') as dpto, "
			+ "(select udpto.descripcion From catalogo.ubigeo as udpto Where udpto.id_ubigeo = substring(:codUbigeo,1,4) || '00') as prov, "
			+ "u.descripcion as dist "
			+ "FROM catalogo.ubigeo as u WHERE u.id_ubigeo = :codUbigeo", 
		nativeQuery = true)
	public IUbigeo getUbigeoFull(String codUbigeo);
	
	@Query("select u from Ubigeo u Where u.idUbigeo like :codUbigeo || '%00' and u.idUbigeo != :codUbigeo || '0000' ")
	public List<Ubigeo> findProvs(String codUbigeo);
	
	@Query("select u from Ubigeo u Where u.idUbigeo like :codUbigeo || '%' and u.idUbigeo != :codUbigeo || '00' ")
	public List<Ubigeo> findDists(String codUbigeo);

}
