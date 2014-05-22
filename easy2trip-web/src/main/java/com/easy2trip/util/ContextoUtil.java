package com.easy2trip.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.easy2trip.controller.SessionMB;


public class ContextoUtil {

	public static SessionMB getUserBean() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext external = context.getExternalContext();
		HttpSession session = (HttpSession) external.getSession(true);

		SessionMB userMB = (SessionMB) session.getAttribute("sessionMB");
		
		return userMB;
	}
	
	public static List<Locale> getIdiomas() {
		FacesContext context = FacesContext.getCurrentInstance();
		Iterator<Locale> locales = context.getApplication().getSupportedLocales();
		List<Locale>	idiomas = new ArrayList<Locale>();
		while (locales.hasNext()) {
			idiomas.add(locales.next());
		}
		return idiomas;
	}
	
	public static Locale getLocaleBrowser(){

		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext external = context.getExternalContext();
		HttpServletRequest req = (HttpServletRequest)external.getRequest();
		Locale locale = req.getLocale();
		Enumeration<String> teste = req.getHeaderNames();
		return locale;
	}

}
