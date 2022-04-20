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

import com.app.checkmoney.model.Transaccion;
import com.app.checkmoney.repository.TransaccionRepository;

/**
 * @author Linygn Escudero
 *
 */
@Service
public class TransaccionService {
	
	@Autowired
	private TransaccionRepository transaccionRepository;
	
	public Transaccion insert(Transaccion tipoDocumento) {
		return transaccionRepository.save(tipoDocumento);
	}
	
	public void update(Transaccion item) {
		transaccionRepository.save(item);
	}
	
	public void delete (Transaccion item) {
		transaccionRepository.save(item);		
	}
	
	public List<Transaccion> findAll() {
		return (List<Transaccion>) transaccionRepository.findAll();
	}
	
	public Transaccion findById(Integer id) {
		return transaccionRepository.findById(id).orElse(null);
	}
	
	public List<Transaccion> findAll(String query, String sortBy) {
		Sort sort;
		if (!sortBy.equals("")) {
			String sortColumn = sortBy.split("\\|")[0];
			String sortDirection = sortBy.split("\\|")[1].toUpperCase();
			sort = Sort.by(sortDirection.equals("DESC") ? Direction.DESC : Direction.ASC, sortColumn);
		} else {
			sort = Sort.by(Direction.ASC, "idTransaccion");
		}
		return transaccionRepository.findAll("%" + query.toLowerCase() + "%", sort);
	}

	public HashMap<String, Object> findAll(String fechaOperacion, String query, int page, int limit, String sortBy) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		Pageable pageable;
		if (!sortBy.equals("")) {
			String sortColumn = sortBy.split("\\|")[0];
			String sortDirection = sortBy.split("\\|")[1].toUpperCase();

			Sort sort = Sort.by(sortDirection.equals("DESC") ? Direction.DESC : Direction.ASC, sortColumn);
			pageable = PageRequest.of(page - 1, limit, sort);

		} else {
			Sort sort = Sort.by(Direction.ASC, "idTransaccion");
			pageable = PageRequest.of(page - 1, limit, sort);

		}
		Page<Transaccion> data = transaccionRepository.findAllParams(fechaOperacion, "%" + query.toLowerCase() + "%", pageable);
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
