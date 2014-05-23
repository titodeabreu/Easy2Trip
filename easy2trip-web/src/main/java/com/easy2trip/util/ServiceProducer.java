package com.easy2trip.util;

import java.util.Hashtable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.logging.Logger;


@ApplicationScoped
public class ServiceProducer {
	@Inject
	private Logger logger;

	private static final String APP_NAME = "pae";
	
	private static final String MODULE_NAME = "pae-core";

	private Hashtable<String, String> jndiProperties;

	@SuppressWarnings("unchecked")
	private <T> T localizar(Class<T> interfaceRemota) {
		String nomeBean = interfaceRemota.getSimpleName().replaceAll("Remote",
				"");
		String enderecoEJB = String.format("ejb:%s/%s//%s!%s", APP_NAME,
				MODULE_NAME, nomeBean, interfaceRemota.getName());
		try {
			final Context context = new InitialContext(jndiProperties);
			return (T) context.lookup(enderecoEJB);
		} catch (NamingException e) {
			String mensagem = String.format(
					"Erro ao localizar %s no endereço %s", interfaceRemota.getSimpleName(),
					enderecoEJB);
			logger.error(mensagem);
			throw new RuntimeException(mensagem, e);
		}
	}

	@PostConstruct
	private void iniciar() {
		//this.jndiProperties = new Hashtable<>();
		this.jndiProperties.put(Context.URL_PKG_PREFIXES,
				"org.jboss.ejb.client.naming");
	}
	
/*
	@Produces
	public ArquivoFinanceiroPaeApplicationServiceRemote arquivoFinanceiroPaeApplicationServiceRemote() {
		return localizar(ArquivoFinanceiroPaeApplicationServiceRemote.class);
	}*/
}

