/**
 * 
 */
package com.app.checkmoney.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.app.checkmoney.model.Perfil;
import com.app.checkmoney.repository.PerfilRepository;

/**
 * @author Linygn Escudero
 *
 */
@Service
public class PerfilService {
	
	@Autowired
	private PerfilRepository perfilRepository; 
	
	public Perfil insert(Perfil item) {
		return perfilRepository.save(item);
	}
	
	public void update(Perfil item) {
		perfilRepository.save(item);
	}
	
	public void delete (Perfil item) {
		perfilRepository.save(item);		
	}
	
	public List<Perfil> findAll() {
		return (List<Perfil>) perfilRepository.findAll();
	}
	
	public Perfil findById(Integer id) {
		return perfilRepository.findById(id).orElse(null);
	}
	
	public List<Perfil> findAll(Integer idSistema, String query, String sortBy) {
		Sort sort;
		if (!sortBy.equals("")) {
			String sortColumn = sortBy.split("\\|")[0];
			String sortDirection = sortBy.split("\\|")[1].toUpperCase();
			sort = Sort.by(sortDirection.equals("DESC") ? Direction.DESC : Direction.ASC, sortColumn);
		} else {
			sort = Sort.by(Direction.ASC, "idPerfil");
		}
		return perfilRepository.findAll(idSistema, "%" + query.toLowerCase() + "%", sort);
	}

	public HashMap<String, Object> findAll(Integer idSistema, String query, int page, int limit, String sortBy) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		Pageable pageable;
		if (!sortBy.equals("")) {
			String sortColumn = sortBy.split("\\|")[0];
			String sortDirection = sortBy.split("\\|")[1].toUpperCase();

			Sort sort = Sort.by(sortDirection.equals("DESC") ? Direction.DESC : Direction.ASC, sortColumn);
			pageable = PageRequest.of(page - 1, limit, sort);

		} else {
			Sort sort = Sort.by(Direction.ASC, "idPerfil");
			pageable = PageRequest.of(page - 1, limit, sort);

		}
		Page<Perfil> data = perfilRepository.findAllParams(idSistema, "%" + query.toLowerCase() + "%", pageable);
		if (!data.getContent().isEmpty()) {
			result.put("items", data.getContent());
		} else {
			result.put("items", new ArrayList<>());
		}
		result.put("totalPage", data.getTotalPages());
		result.put("totalRows", data.getNumberOfElements());
		result.put("totalItems", data.getTotalElements());
		result.put("page", page);
		result.put("sizeRows", limit);
		return result;
	}

}
