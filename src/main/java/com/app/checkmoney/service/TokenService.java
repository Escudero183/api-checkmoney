package com.app.checkmoney.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.checkmoney.model.Token;
import com.app.checkmoney.repository.TokenRepository;



@Service
@Transactional
public class TokenService {

	@Autowired
	private TokenRepository tokenRepository;

	public void insert(Token token) {
		tokenRepository.save(token);
	}

	public boolean checkTokenByUsuario(String token, Integer idUsuario, String userAgent) {
		Token tokenBean = tokenRepository.findByHashAndUsuario(token, idUsuario, userAgent);
		return tokenBean != null;
	}

	public Token findTokenByUsuario(String token, Integer idUsuario, String userAgent) {
		return tokenRepository.findByHashAndUsuario(token, idUsuario, userAgent);
	}

	public void delete(Token tokenBean) {
		tokenRepository.delete(tokenBean);
	}
}
