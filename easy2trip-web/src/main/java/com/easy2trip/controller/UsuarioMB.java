package com.easy2trip.controller;

import java.io.IOException;
import java.util.*;

import javax.ejb.EJB;
import javax.faces.bean.*;
import javax.faces.context.*;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.UploadedFile;

import com.easy2trip.exception.RNException;
import com.easy2trip.model.Usuario;
import com.easy2trip.services.UsuarioLocal;
import com.easy2trip.services.UsuarioRemote;
import com.easy2trip.util.ContextoUtil;
import com.easy2trip.util.SalvaArquivoHelper;

/** ManagedBean responsavel por efetuar as opera��es de usuario como: login,
 * registro, altera��o de dados, upload de foto, recupera�ao de senha e desativacao de conta.
 * 
 * @author Mario Jorge
 */
@ManagedBean(name="usuarioMB")
@RequestScoped
public class UsuarioMB extends AbstractMB{

	private static final long serialVersionUID = 1L;
		
	@EJB
	private UsuarioRemote usuarioEJB;
	private Usuario	    usuario	= new Usuario();
	private Usuario 	usuarioEmRecuperacao = new Usuario();
	private List<SelectItem> idiomas = new ArrayList<SelectItem>();
	private  UploadedFile  file;
	private  CroppedImage  croppedImage;
	private String pathFotoPadrao =  ("/" + "resources"+ "/" + "default");
	private String pathFotoPerfil;
	private String username;
	private String password;
		
	public String novoRegistro() {
		this.usuario = new Usuario();
		this.usuario.setAtivo(true);
		this.usuario.setTemFoto(false);
		return "registro";
	}
	
	public String editar() {	
		if(getUsuarioLogado() != null){
			usuario = getUsuarioLogado();
		}
		return "atualizaperfil";
	}

	public String principal(){
		return "principal";
	}

	public String login(){
		return "login";
	}
	
	/**
	 * m�todo de login pragm�tico com JAAS
	 * @return
	 */
	public String efetuarLogin(){
			
		try {       
	            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	            request.login(username, password);
	           
	            if(request.isUserInRole("USER")){
	            	return "teste";}
	            
	            else if(request.isUserInRole("ADMIN")){
	            	return "admin";
	            	
	            }else {
	            	addMensagemAlerta("msg_loginousenhainvalido");
	            	return null;
	            }

	        } catch(Exception e) {
	        	addMensagemAlerta("msg_loginousenhainvalido");
	        }
	        return null;
	}
		
	/**
	 * Metodo que Inclui um novo usu�rio ou a.
	 */
	public String salvar() {

		ExternalContext external = FacesContext.getCurrentInstance().getExternalContext();
						
		if(usuario.getId()==null){
			try {
				usuario.setPathFoto(this.pathFotoPadrao);
				usuarioEJB.save(usuario);
				addMensagemInformativa("msg_cadastroefetuado", usuario.getLogin());
				
	            try {
	            	HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
					request.login(usuario.getLogin(), usuario.getPassword());
										
					return "/protected/user/teste";
					
				} catch (ServletException e) {
					addMensagemErro("msg_errogeral");
					return null;
				}
	            
			} catch (RNException e) {
				addMensagemAlerta(e.getChaveErro());
				return null;
			}
		}else{
			try {
				
				usuario = usuarioEJB.update(usuario);
				addMensagemInformativa("msg_dadosalteradossucesso");
				
				if(!usuario.getLogin().equals(getUsuarioLogado().getLogin())){
					external.invalidateSession();
					return "login";
				}
				return null;

			} catch (RNException e) {
				addMensagemAlerta(e.getChaveErro());
				return null;
			}
		}
	}
	
	/**
	 * Metodo que efetua a recupera��o de senha e envio esta para o email do usuario
	 * 
	 */
	public String recuperaSenha() {
		try {
			usuarioEJB.recuperaSenha(getUsuarioEmRecuperacao());
			addMensagemInformativa("msg_reenviodesenhasucesso");
			return "login";
		} catch (RNException e) {
			addMensagemAlerta(e.getChaveErro());
			return null;
		}
	}
	
	/**
	 * Metodo que efetua a desativa��o do usuario
	 */
	public String desativar() {

		ExternalContext external = FacesContext.getCurrentInstance().getExternalContext();
		
		try {
			usuarioEJB.desativaUsuario(usuario);
			addMensagemAlerta("msg_desativacaocontasucesso");
			
		} catch (RNException e) {
			addMensagemAlerta(e.getChaveErro());
		}
		external.invalidateSession();
		return "login";
		
	}
	
	/**
	 * Metodo acionado por evento quando selecionado uma imagem na tela de altera��o
	 * e salva esta e 2 tamanhos a maior como ID do usuario e a outra com o ID do usuario mais "_p" ambas como .jpg
	 * 
	 */	
	public String handleFileUpload(FileUploadEvent  event){
		UploadedFile  file  =  event.getFile();
		ServletContext  servletContext  =  (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();	
		SalvaArquivoHelper salvaArquivo = new SalvaArquivoHelper();
				
		try {
			salvaArquivo.salvarImagemReduzida(
			    getUsuarioLogado().getId().toString(), 
				file, 
				servletContext.getRealPath("")
							);				
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		Usuario u =  getUsuarioLogado();

		u.setTemFoto(true);
		u.setPathFoto(("/" + "resources"+ "/" + getUsuarioLogado().getId().toString()));
		try {
			usuario = usuarioEJB.update(u);
			
		} catch (RNException e) {
			addMensagemAlerta(e.getMessage());
		}

		pathFotoPerfil = null;
		return "/protected/user/perfil";
	}
	
	/**
	 * M�todo do componete primefaces CroppedImage que recorta a imagem da tela.
	 * @return
	 */
	public  String  crop()  {
		
		ServletContext  servletContext  =  (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		SalvaArquivoHelper salva = new SalvaArquivoHelper();
		salva.crop(servletContext.getRealPath(""), croppedImage);
		//urlImagem = null;
		
		return  "/restrito/atualizaperfil";
	}
	
	
	//================= Getters e Setters ==========================
	
	public Usuario getUsuario() {
		if(usuario == null || getUsuarioLogado() !=null){
			usuario = getUsuarioLogado();
		}
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuarioEmRecuperacao() {
		return usuarioEmRecuperacao;
	}


	public void setUsuarioEmRecuperacao(Usuario usuarioEmRecuperacao) {
		this.usuarioEmRecuperacao = usuarioEmRecuperacao;
	}
	
	/**
	 * Retonar lista de selectIem de idiomas existentes
	 * @return
	 */
	public List<SelectItem> getIdiomas() {
		
		if(idiomas.size()< 1){
			
			Locale localeDoUsuario = ContextoUtil.getLocaleBrowser();
			Locale[] locales = usuarioEJB.getSystemLocales(localeDoUsuario);
			
			for(Integer i=0; i < locales.length; i++){
				String underscore = "_";
				if(locales[i].getCountry().length() < 1){
					underscore = "";
				}
				String lang = (locales[i].getLanguage() + underscore +locales[i].getCountry());
				
				idiomas.add(new SelectItem(locales[i].getLanguage() + underscore +locales[i].getCountry(), locales[i].getDisplayName()));
				String idioma = localeDoUsuario.toString();
				
				if(usuario.getIdioma() == null || usuario.getIdioma().length() == 0){
					if(lang.equals(idioma)){
						usuario.setIdioma(locales[i].getLanguage() + underscore +locales[i].getCountry());
					}
				}
			}
		}
		return idiomas;
	}
	
	public void setIdiomas(ArrayList<SelectItem> idiomas) {
		this.idiomas = idiomas;
		}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public CroppedImage getCroppedImage() {
		return croppedImage;
	}

	public void setCroppedImage(CroppedImage croppedImage) {
		this.croppedImage = croppedImage;
	}


	public String getPathFotoPerfil() {
		if(pathFotoPerfil == null){
			pathFotoPerfil = getUsuarioLogado().getPathFoto();
		}
		return pathFotoPerfil;
	}

	public void setPathFotoPerfil(String pathFotoPerfil) {
		this.pathFotoPerfil = pathFotoPerfil;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}


