package com.easy2trip.exception;

public class RNException extends Exception {

	private static final long serialVersionUID = 1L;
	private final String chaveErro;
	private final Object[] arguments;
	
	/**
	 * @param chaveErro Chave da properties da vis�o com a mensagem a ser exibida.
	 * @param t Causa do erro, pode ser null
	 * @param arguments Argumentos utilizados como par�metros para a mensagem. Ex: campos que s�o obrigat�rios
	 */
	public RNException(String chaveErro, Throwable t, Object... arguments) {
		super(t);
		this.chaveErro = chaveErro;
		this.arguments = arguments;
	}
	
	public RNException(String msgErro) {
		this(msgErro, null, (Object) null);
	}

	public String getChaveErro() {
		return chaveErro;
	}
	
	public Object[] getArguments() {
		return arguments;
	}
}
