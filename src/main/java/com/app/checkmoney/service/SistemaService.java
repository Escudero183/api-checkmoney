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

import com.app.checkmoney.model.Sistema;
import com.app.checkmoney.repository.SistemaRepository;

/**
 * @author Linygn Escudero
 *
 */
@Service
public class SistemaService {
	
	@Autowired
	private SistemaRepository sistemaRepository;
	
	public Sistema insert(Sistema item) {
		return sistemaRepository.save(item);
	}
	
	public void update(Sistema item) {
		sistemaRepository.save(item);
	}
	
	public void delete (Sistema item) {
		sistemaRepository.save(item);		
	}
	
	public List<Sistema> findAll() {
		return (List<Sistema>) sistemaRepository.findAll();
	}
	
	public Sistema findById(Integer id) {
		return sistemaRepository.findById(id).orElse(null);
	}
	
	public List<Sistema> findAll(String query, String sortBy) {
		Sort sort;
		if (!sortBy.equals("")) {
			String sortColumn = sortBy.split("\\|")[0];
			String sortDirection = sortBy.split("\\|")[1].toUpperCase();
			sort = Sort.by(sortDirection.equals("DESC") ? Direction.DESC : Direction.ASC, sortColumn);
		} else {
			sort = Sort.by(Direction.ASC, "idSistema");
		}
		return sistemaRepository.findAll("%" + query.toLowerCase() + "%", sort);
	}

	public HashMap<String, Object> findAll(String query, int page, int limit, String sortBy) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		Pageable pageable;
		if (!sortBy.equals("")) {
			String sortColumn = sortBy.split("\\|")[0];
			String sortDirection = sortBy.split("\\|")[1].toUpperCase();

			Sort sort = Sort.by(sortDirection.equals("DESC") ? Direction.DESC : Direction.ASC, sortColumn);
			pageable = PageRequest.of(page - 1, limit, sort);

		} else {
			Sort sort = Sort.by(Direction.ASC, "idSistema");
			pageable = PageRequest.of(page - 1, limit, sort);

		}
		Page<Sistema> data = sistemaRepository.findAllParams("%" + query.toLowerCase() + "%", pageable);
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
