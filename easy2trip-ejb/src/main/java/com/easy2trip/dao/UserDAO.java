package com.easy2trip.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import com.easy2trip.model.Usuario;

@Stateless
public class UserDAO extends GenericDAO<Usuario, Long> {
	
	public UserDAO() {
		super(Usuario.class);
	}
	
	public Usuario findUserByEmail(String email){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("email", email);		
		return super.findOneResult(Usuario.NQ_BUSCAPOREMAIL, parameters);
	}
	
	public Usuario findUserByLogin(String login){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("login", login);		
		return super.findOneResult(Usuario.NQ_BUSCAPORLOGIN, parameters);
	}
	
	public List<Usuario> findbyNomeEmailOuLogin(String criterio) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("palavra", "%" + criterio + "%");		
		return super.findResults(Usuario.NQ_BUSCAPORNOMEEMAILOULOGIN, parameters);
	}
}