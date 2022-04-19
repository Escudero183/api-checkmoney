/**
 * 
 */
package com.app.checkmoney.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.checkmoney.model.UsuarioSistema;
import com.app.checkmoney.repository.UsuarioSistemaRepository;

/**
 * @author Linygn Escudero
 *
 */

@Service
public class UsuarioSistemaService {
	
	@Autowired
	private UsuarioSistemaRepository usuarioSistemaRepository;
	
	public UsuarioSistema insert(UsuarioSistema usuario) {
		return usuarioSistemaRepository.save(usuario);
	}
	
	public void update(UsuarioSistema item) {
		usuarioSistemaRepository.save(item);
	}
	
	public void delete (UsuarioSistema item) {
		usuarioSistemaRepository.save(item);		
	}
	
	public List<UsuarioSistema> findByUsuario(Integer idUsuario) {
		return (List<UsuarioSistema>) usuarioSistemaRepository.findByUsuario(idUsuario);
	}

}
