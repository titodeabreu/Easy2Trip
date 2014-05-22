package com.easy2trip.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.easy2trip.model.Usuario;
import com.easy2trip.util.ContextoUtil;
import com.easy2trip.util.MensagemUtil;


public abstract class AbstractMB implements Serializable {
	
	protected static final String SUCESSO = "sucesso";
	protected static final String ERRO = "erro";
	protected static final String INICIO = "inicio";
	private static final long serialVersionUID = 1L;
	private static final String FACES_CONFIG_RESOURCE_VAR_NAME = "msgs";
	private static final String FACES_CONFIG_PROPERTIES_RESOURCE_VAR_NAME = "propriedades";
		
	private void addMensagem(Severity severity, String messageKey, Object... args) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage("mensagem", new FacesMessage(severity,"",MensagemUtil.getMensagem(messageKey, args)));
	}
	
	protected final void addMensagemErro(String messageKey, Object... args) {
		addMensagem(FacesMessage.SEVERITY_ERROR, messageKey, args);
	}
	
	protected final void addMensagemAlerta(String messageKey, Object... args) {
		addMensagem(FacesMessage.SEVERITY_WARN, messageKey, args);
	}

	protected final void addMensagemInformativa(String messageKey, Object... args) {
		addMensagem(FacesMessage.SEVERITY_INFO, messageKey, args);
	}
	
	protected final void addMensagemFatal(String messageKey, Object... args) {
		addMensagem(FacesMessage.SEVERITY_FATAL, messageKey, args);
	}
	
	/**
	 * Retorna o valor do parametro no request
	 * @see ExternalContext#getRequestMap().get(Object)
	 * @param parameterName
	 * @return
	 */
	protected final Object getRequestParameter(String parameterName) {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(parameterName);
	}
	
	protected void sendRedirect(String url) {
		try {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext(); 
			ec.redirect(ec.getRequestContextPath() + url);
			
		} catch (IOException e) {
			//LOG.debug("Erro ao fazer redirect [{0}]", e, url);
			throw new IllegalStateException("Erro ao fazer redirect", e);
		}
	}
	
	public Usuario getUsuarioLogado(){
		Usuario usuario = ContextoUtil.getUserBean().getUser();
		//TODO verificar se � necess�rio o c�digo abaixo
		/*if(usuario == null){
			addMensagemErro("msg_usuarionaologado");
			sendRedirect("/publico/login.xhtml");
		}*/
		return usuario;
	}
			
	/*protected void addMensagemErro(RNException negocioException) {
		addMensagemErro(negocioException.getCodigoErro(), negocioException.getArguments());
	}

	protected void addMensagemErro(Integer codigoErro, Object ... args) {
		//addMensagemErro(codigoErro.getCodigoMensagem().getCodigo(), args);
	}

	protected final void addMensagemInformativa(Integer codigoMensagem, Object... args) {
		//addMensagemInformativa(codigoMensagem.getCodigo(), args);
	}
	
	protected final void addMensagemAlerta(Integer codigoMensagem, Object... args) {
		//addMensagemAlerta(codigoMensagem.getCodigo(), args);
	}
*/
			
	public  String getPropriedade(String propertyKey) {
		ResourceBundle bundle = ResourceBundle.getBundle(FACES_CONFIG_PROPERTIES_RESOURCE_VAR_NAME);
		String messageValue = bundle.getString(propertyKey);
		return MessageFormat.format(messageValue, (Object[]) null);
	}
	
	public ResourceBundle getDefaultResourceBundle() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		return ctx.getApplication().getResourceBundle(ctx, FACES_CONFIG_RESOURCE_VAR_NAME);
	}

}
