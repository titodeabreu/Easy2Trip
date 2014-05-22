package com.easy2trip.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.PropertyPermission;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.easy2trip.dao.UserDAO;
import com.easy2trip.exception.RNException;
import com.easy2trip.model.Usuario;


@Stateless
public class UsuarioEJB implements UsuarioLocal,UsuarioRemote {

	@EJB 
	private UserDAO userDAO;
	
	/*@EJB
	private AmigoDAO amigoDAO;*/
	
	public Usuario findUserByEmail(String email) {
		return userDAO.findUserByEmail(email);
	}
	
	@Override
	public Usuario findUserByLogin(String login) {
		return userDAO.findUserByLogin(login);
	}
	
	@Override
	public List<Usuario> findByNomeEmailOuLogin(String criterio) {
		return userDAO.findbyNomeEmailOuLogin(criterio);
	}
	
	
	/**
	 * Efetua a busca de usuario pelo nome ou pelo login conforme o crit�rio informado.
	 * Caso esteja vazia o parametro ("") o sistema recupera os usu�rios que s�o j� s�o amigos 
	 * do usu�rio passado. 
	 * @param idUsuario
	 * @param palavra
	 * @return Lista de usuario encontrados.
	 *//*
	public List<Usuario> buscaPorNomeOuLogin(Long idUsuario,String palavra){
		List<Usuario> lista = null;
		if(palavra != null && !palavra.equals("")){
			lista = userDAO.findbyNomeEmailOuLogin(palavra);
			
		}else if(palavra != null && palavra.equals("")){
			List<Amigo> amigos = amigoDAO.findAmigosByUsuario(idUsuario);
			if(amigos != null){
				lista = new ArrayList<Usuario>();
				for(Amigo a: amigos){
					lista.add(a.getAmigo());
				}
			}
		}
		return lista;
	}*/
	
	@Override
	public void save(Usuario usuario) throws RNException {
		
		if(isUsuarioValido(usuario)){
			usuario.setId(null);
			usuario.getLogin().toLowerCase();
			usuario.getEmail().toLowerCase();
			
			Usuario usuarioComMesmoEmail = userDAO.findUserByEmail(usuario.getEmail());
			Usuario usuarioComMesmoLogin = userDAO.findUserByLogin(usuario.getLogin());
		
			if(usuarioComMesmoEmail != null){
				throw new RNException("msg_emailjacadastrado");
			}
			if(usuarioComMesmoLogin != null){
				throw new RNException("msg_loginjaexiste");
			}
			
			if(usuario.getId() == null){	
				
				Date data = new Date(System.currentTimeMillis());
				usuario.setTs(data);
				usuario.setTsCriacao(data);
				usuario.setAtivo(true);
				usuario.setRole("USER");
				userDAO.save(usuario);
				//envia email de cadastro
				/*try {
					UsuarioRN usuarioRN = new UsuarioRN();
					usuarioRN.enviarEmailPosCadastramento(this.usuario);
				} catch (RNException e) {
					e.printStackTrace();
				} */
			}
		}else{
			throw new RNException("msg_usuariocomdadosobrigatoriosnaoinformado");
		}
	}

	@Override
	public Usuario update(Usuario usuario) throws RNException{
	
		if(isUsuarioValido(usuario) && usuario.getId() != null){
			usuario.getLogin().toLowerCase();
			usuario.getEmail().toLowerCase();
			Usuario usuarioComMesmoEmail = userDAO.findUserByEmail(usuario.getEmail());
			Usuario usuarioComMesmoLogin = userDAO.findUserByLogin(usuario.getLogin());

			if(usuarioComMesmoEmail != null && !usuarioComMesmoEmail.getId().equals(usuario.getId())){
				throw new RNException("msg_emailjacadastrado");
			}
			if(usuarioComMesmoLogin != null && !usuarioComMesmoLogin.getId().equals(usuario.getId())){
				throw new RNException("msg_loginjaexiste");
			}
			
			if(usuario.getPassword().equals("")){
				usuario.setPassword(userDAO.find(usuario.getId()).getPassword());
			}
			
			Date data = new Date((System.currentTimeMillis()));
			usuario.setTs(data);
			usuario.setAtivo(true);
			usuario.setRole("USER");																			
			return userDAO.update(usuario);
		}else{
			throw new RNException("msg_usuariocomdadosobrigatoriosnaoinformado");
		}
	}
	
	@Override
	public void recuperaSenha(Usuario usuario) throws RNException {
		Usuario usuarioComMesmoEmail = userDAO.findUserByEmail(usuario.getEmail());

		if(usuarioComMesmoEmail != null){
			if(usuario.getNascimento().equals(usuarioComMesmoEmail.getNascimento())){
				if(!usuarioComMesmoEmail.isAtivo()){
					usuarioComMesmoEmail.setAtivo(true);
				}
				userDAO.update(usuarioComMesmoEmail);
				/*try {
					this.usuarioEmRecuperacao.setIdioma(usuarioComMesmoEmail.getIdioma());
					rn.enviarEmailRecuperacaoSenha(usuarioComMesmoEmail);
				} catch (RNException e) {
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Ops...solicite sua senha novamente em alguns minutos por favor."));
					e.printStackTrace();
					return null;
				} */				
			}else{
				throw new RNException("msg_reenviodesenhadadosinvalidos");
			}
		}else {
			throw new RNException("msg_reenviodesenhadadosinvalidos");
		}
	}
	
	@Override
	public void desativaUsuario(Usuario usuario) throws RNException {
		Date data = new Date((System.currentTimeMillis()));
		usuario.setTs(data);
		usuario.setAtivo(false);
		userDAO.update(usuario);
		
	}
	
	public void delete(Usuario usuario) {
		userDAO.delete(usuario);
	}

	@Override
	public Usuario find(Long entityID) {
		return userDAO.find(entityID);
	}

	@Override
	public void enviaEmailPosCadastramento(Usuario usuario) throws RNException {
		// TODO Auto-generated method stub
	}

	@Override
	public Locale[] getSystemLocales(Locale locale) {
		//TODO deixar a JVM retornar??
		new PropertyPermission("user.language", "write");
		Locale.setDefault(locale);
		Locale[] locales = Locale.getAvailableLocales();
		return locales;
	}
	
	protected boolean isUsuarioValido(Usuario usuario){
		if(usuario.getLogin() != null &&
				usuario.getEmail() != null &&
				usuario.getPassword() != null &&
				usuario.getPathFoto() != null
			){
			return true;
		}
		return false;
	}	
}