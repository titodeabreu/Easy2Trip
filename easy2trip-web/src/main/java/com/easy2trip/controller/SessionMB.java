package com.easy2trip.controller;

import java.util.Enumeration;
import java.util.Locale;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.easy2trip.model.Usuario;
import com.easy2trip.services.UsuarioLocal;

/**
 * Bean de Sess�o com os dados do usu�rio logado
 * @author mjlcalmeida
 *
 */
@SessionScoped
@ManagedBean(name="sessionMB")
public class SessionMB {
	private Usuario user;
	
	@EJB
	private UsuarioLocal userFacade;
	private Locale	      localizacao	  = null;
	
	/**
	 * Recupera o usu�rio do banco conforme o email
	 * @return
	 */
	public Usuario getUser(){
		if(user == null){
			String userLogin = null;
			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			userLogin = context.getRemoteUser();
			if(userLogin != null){
				user = userFacade.findUserByLogin(userLogin);
			}
		}
		return user;
	}
	
	/**
	 * Recupera do usu�rio logado a locale
	 * @return
	 */
	public Locale getLocaleUsuario() {
		if(user != null){
			if (this.localizacao == null) {
				String[] info = user.getIdioma().split("_");
				this.localizacao = new Locale(info[0], info[1]);
			}
		}
		return this.localizacao;
	}
	
	/**
	 * Recupera as informa��es do browser do contexto atual
	 * @return
	 */
	public String getUserInfo(){

		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext external = context.getExternalContext();
		HttpServletRequest req = (HttpServletRequest)external.getRequest();

		Enumeration<String> headerNames = req.getHeaderNames();
		String info = "";

		while (headerNames.hasMoreElements()) {
			String headerName = (String) headerNames.nextElement();
			String headerValue = req.getHeader(headerName);
			info+=headerName;
			info+=headerValue;
			info+="\n";
		}
		return info;
	}
		
	/*public void setIdiomaUsuario(String idioma) {
		UsuarioRN usuarioRN = new UsuarioRN();
		this.usuarioLogado = usuarioRN.carregar(this.getUsuarioLogado().getCodigo());
		this.usuarioLogado.setIdioma(idioma);
		usuarioRN.salvar(this.usuarioLogado);
		if(user != null){
			String[] info = idioma.split("_");
			Locale locale = new Locale(info[0], info[1]);

			FacesContext context = FacesContext.getCurrentInstance();
			context.getViewRoot().setLocale(locale);
		}
	}*/
	
	public boolean isUserAdmin(){
		
		return getRequest().isUserInRole("ADMIN");
	}
	
	public String logOut(){
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		request.getSession().invalidate();		
		return "login";
	}

	private HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}

}