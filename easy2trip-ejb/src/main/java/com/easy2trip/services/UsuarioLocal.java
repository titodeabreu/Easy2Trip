package com.easy2trip.services;

import java.util.List;
import java.util.Locale;

import javax.ejb.Local;

import com.easy2trip.exception.RNException;
import com.easy2trip.model.Usuario;

@Local
public interface UsuarioLocal {
	
	public Usuario findUserByEmail(String email);
	
	public Usuario findUserByLogin(String login);
	
	public void save(Usuario usuario) throws RNException;

	public Usuario update(Usuario usuario) throws RNException;
	
	public void recuperaSenha(Usuario usuario) throws RNException;
	
	public void desativaUsuario(Usuario usuario) throws RNException;
	
	//public List<Usuario> buscaPorNomeOuLogin(Long idUsuario,String palavra);
	
	public Usuario find(Long entityID);
	
	public void enviaEmailPosCadastramento(Usuario usuario) throws RNException;
	
	public List<Usuario> findByNomeEmailOuLogin(String criterio);
	
	public Locale[] getSystemLocales(Locale locale);
	
	
}