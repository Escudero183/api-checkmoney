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

import com.app.checkmoney.model.Modulo;
import com.app.checkmoney.repository.ModuloRepository;

/**
 * @author Linygn Escudero
 *
 */
@Service
public class ModuloService {
	
	@Autowired
	private ModuloRepository moduloRepository; 
	
	public Modulo insert(Modulo item) {
		return moduloRepository.save(item);
	}
	
	public void update(Modulo item) {
		moduloRepository.save(item);
	}
	
	public void delete (Modulo item) {
		moduloRepository.save(item);		
	}
	
	public List<Modulo> findAll() {
		return (List<Modulo>) moduloRepository.findAll();
	}
	
	public Modulo findById(Integer id) {
		return moduloRepository.findById(id).orElse(null);
	}
	
	public List<Modulo> findAll(Integer idSistema, String query, String sortBy) {
		Sort sort;
		if (!sortBy.equals("")) {
			String sortColumn = sortBy.split("\\|")[0];
			String sortDirection = sortBy.split("\\|")[1].toUpperCase();
			sort = Sort.by(sortDirection.equals("DESC") ? Direction.DESC : Direction.ASC, sortColumn);
		} else {
			sort = Sort.by(Direction.ASC, "idModulo");
		}
		return moduloRepository.findAll(idSistema, "%" + query.toLowerCase() + "%", sort);
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
			Sort sort = Sort.by(Direction.ASC, "idModulo");
			pageable = PageRequest.of(page - 1, limit, sort);

		}
		Page<Modulo> data = moduloRepository.findAllParams(idSistema, "%" + query.toLowerCase() + "%", pageable);
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
