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

import com.app.checkmoney.model.Divisa;
import com.app.checkmoney.repository.DivisaRepository;

/**
 * @author Linygn Escudero
 *
 */
@Service
public class DivisaService {
	
	@Autowired
	private DivisaRepository divisaRepository;
	
	public Divisa insert(Divisa tipoDocumento) {
		return divisaRepository.save(tipoDocumento);
	}
	
	public void update(Divisa item) {
		divisaRepository.save(item);
	}
	
	public void delete (Divisa item) {
		divisaRepository.save(item);		
	}
	
	public List<Divisa> findAll() {
		return (List<Divisa>) divisaRepository.findAll();
	}
	
	public Divisa findById(Integer id) {
		return divisaRepository.findById(id).orElse(null);
	}
	
	public List<Divisa> findAll(String query, String sortBy) {
		Sort sort;
		if (!sortBy.equals("")) {
			String sortColumn = sortBy.split("\\|")[0];
			String sortDirection = sortBy.split("\\|")[1].toUpperCase();
			sort = Sort.by(sortDirection.equals("DESC") ? Direction.DESC : Direction.ASC, sortColumn);
		} else {
			sort = Sort.by(Direction.ASC, "idDivisa");
		}
		return divisaRepository.findAll("%" + query.toLowerCase() + "%", sort);
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
			Sort sort = Sort.by(Direction.ASC, "idDivisa");
			pageable = PageRequest.of(page - 1, limit, sort);

		}
		Page<Divisa> data = divisaRepository.findAllParams("%" + query.toLowerCase() + "%", pageable);
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
