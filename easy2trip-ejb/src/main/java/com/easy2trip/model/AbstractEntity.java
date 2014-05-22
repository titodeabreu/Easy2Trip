package com.easy2trip.model;

import java.io.Serializable;

public interface AbstractEntity<T extends Serializable> extends Serializable {

	/**
	 * @return A referencia para a chave primaria (Primary Key) de cada objeto persistido.
	 * 		   Caso o objeto ainda nao tenha sido persistido, deve retornar <code>null</code>.
	 */
	public abstract T getId();
	public abstract void setId(T id);
	
}
