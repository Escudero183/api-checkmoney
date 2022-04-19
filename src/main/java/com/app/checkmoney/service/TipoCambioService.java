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

import com.app.checkmoney.model.TipoCambio;
import com.app.checkmoney.repository.TipoCambioRepository;

/**
 * @author Linygn Escudero
 *
 */
@Service
public class TipoCambioService {
	
	@Autowired
	private TipoCambioRepository tipoCambioRepository;
	
	public TipoCambio insert(TipoCambio tipoDocumento) {
		return tipoCambioRepository.save(tipoDocumento);
	}
	
	public void update(TipoCambio item) {
		tipoCambioRepository.save(item);
	}
	
	public void delete (TipoCambio item) {
		tipoCambioRepository.save(item);		
	}
	
	public List<TipoCambio> findAll() {
		return (List<TipoCambio>) tipoCambioRepository.findAll();
	}
	
	public TipoCambio findById(Integer id) {
		return tipoCambioRepository.findById(id).orElse(null);
	}
	
	public List<TipoCambio> findAll(String query, String sortBy) {
		Sort sort;
		if (!sortBy.equals("")) {
			String sortColumn = sortBy.split("\\|")[0];
			String sortDirection = sortBy.split("\\|")[1].toUpperCase();
			sort = Sort.by(sortDirection.equals("DESC") ? Direction.DESC : Direction.ASC, sortColumn);
		} else {
			sort = Sort.by(Direction.ASC, "idTipoCambio");
		}
		return tipoCambioRepository.findAll("%" + query.toLowerCase() + "%", sort);
	}

	public HashMap<String, Object> findAll(String fecha, Boolean vigente, String query, int page, int limit, String sortBy) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		Pageable pageable;
		if (!sortBy.equals("")) {
			String sortColumn = sortBy.split("\\|")[0];
			String sortDirection = sortBy.split("\\|")[1].toUpperCase();

			Sort sort = Sort.by(sortDirection.equals("DESC") ? Direction.DESC : Direction.ASC, sortColumn);
			pageable = PageRequest.of(page - 1, limit, sort);

		} else {
			Sort sort = Sort.by(Direction.ASC, "idTipoCambio");
			pageable = PageRequest.of(page - 1, limit, sort);

		}
		Page<TipoCambio> data = tipoCambioRepository.findAllParams(fecha, vigente, "%" + query.toLowerCase() + "%", pageable);
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
