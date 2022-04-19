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

import com.app.checkmoney.model.Cliente;
import com.app.checkmoney.repository.ClienteRepository;
import com.app.checkmoney.repository.UbigeoRepository;

/**
 * @author Linygn Escudero
 *
 */
@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private UbigeoRepository ubigeoRepository;
	
	public Cliente insert(Cliente item) {
		return clienteRepository.save(item);
	}
	
	public void update(Cliente item) {
		clienteRepository.save(item);
	}
	
	public void delete (Cliente item) {
		clienteRepository.save(item);		
	}
	
	public List<Cliente> findAll() {
		return (List<Cliente>) clienteRepository.findAll();
	}
	
	public Cliente findById(Integer id) {
		return clienteRepository.findById(id).orElse(null);
	}
	
	public List<Cliente> findAll(String query, String sortBy) {
		Sort sort;
		if (!sortBy.equals("")) {
			String sortColumn = sortBy.split("\\|")[0];
			String sortDirection = sortBy.split("\\|")[1].toUpperCase();
			sort = Sort.by(sortDirection.equals("DESC") ? Direction.DESC : Direction.ASC, sortColumn);
		} else {
			sort = Sort.by(Direction.ASC, "idCliente");
		}
		return clienteRepository.findAll("%" + query.toLowerCase() + "%", sort);
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
			Sort sort = Sort.by(Direction.ASC, "idCliente");
			pageable = PageRequest.of(page - 1, limit, sort);

		}
		Page<Cliente> data = clienteRepository.findAllParams("%" + query.toLowerCase() + "%", pageable);
		if (!data.getContent().isEmpty()) {
			for(Cliente cliente : data) {
				cliente.setUbigeoAll(ubigeoRepository.getUbigeoFull(cliente.getUbigeo().getIdUbigeo()));
			}
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
