package com.easy2trip.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.easy2trip.model.AbstractEntity;



public abstract class GenericDAO<T extends AbstractEntity<PK>, PK extends Serializable> {

	private final static String UNIT_NAME = "easy2tripPU";

	@PersistenceContext(unitName = UNIT_NAME)
	private EntityManager em;

	private Class<T> entityClass;

	public GenericDAO(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public void save(T entity) {
		em.persist(entity);
	}

	public void delete(T entity) {
		T entityToBeRemoved = em.merge(entity);

		em.remove(entityToBeRemoved);
	}

	public T update(T entity) {
		return em.merge(entity);
	}

	public T find(PK entityID) {
		return em.find(entityClass, entityID);
	}

	// Using the unchecked because JPA does not have a
	// em.getCriteriaBuilder().createQuery()<T> method
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findAll() {
		CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
		cq.select(cq.from(entityClass));
		return em.createQuery(cq).getResultList();
	}

	public List<T> findRange(int[] range) {
		CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
		cq.select(cq.from(entityClass));
		Query q = em.createQuery(cq);
		q.setMaxResults(range[1] - range[0]);
		q.setFirstResult(range[0]);
		return q.getResultList();
	}

	public int count() {
		CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
		Root<T> rt = cq.from(entityClass);
		cq.select(em.getCriteriaBuilder().count(rt));
		Query q = em.createQuery(cq);
		return ((Long) q.getSingleResult()).intValue();
	}

	// Using the unchecked because JPA does not have a
	// ery.getSingleResult()<T> method
	@SuppressWarnings("unchecked")
	protected T findOneResult(String namedQuery, Map<String, Object> parameters) {
		T result = null;

		try {
			Query query = em.createNamedQuery(namedQuery);

			// Method that will populate parameters if they are passed not null and empty
			if (parameters != null && !parameters.isEmpty()) {
				populateQueryParameters(query, parameters);
			}

			result = (T) query.getSingleResult();

		} catch (NoResultException e) {
			System.out.println("\n Nenhume resultado encontrado");
			return null;
		}

		return result;
	}

	// Using the unchecked because JPA does not have a
	// ery.getSingleResult()<T> method
	@SuppressWarnings("unchecked")
	protected List<T> findResults(String namedQuery, Map<String, Object> parameters) {
		List<T> result = null;

		try {
			Query query = em.createNamedQuery(namedQuery);

			// Method that will populate parameters if they are passed not null and empty
			if (parameters != null && !parameters.isEmpty()) {
				populateQueryParameters(query, parameters);
			}

			result = query.getResultList();

		} catch (Exception e) {
			System.out.println("Error while running query: " + e.getMessage());
			//TODO retirar
			e.printStackTrace();
		}

		return result;
	}

	private void populateQueryParameters(Query query, Map<String, Object> parameters) {

		for (Entry<String, Object> entry : parameters.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
	}

	//============================================================================


	@SuppressWarnings("unchecked")
	protected T getSingleResult(Query q) {
		try {
			return (T) q.getSingleResult();
		} catch (NoResultException e) {
			//TODO
			e.printStackTrace();
			return null;
		}
	}

	protected T getSingleResultByNamedQuery(String namedQuery, Map<String, Object> parameters) {
		Query query = createNamedQuery(namedQuery, parameters);

		return getSingleResult(query);
	}

	@SuppressWarnings("unchecked")
	protected List<T> getResultListByNamedQuery(String namedQuery, Map<String, Object> parameters) {
		Query query = createNamedQuery(namedQuery, parameters);

		return query.getResultList();
	}

	protected T getSingleResultByQuery(String queryCommand, List<Object> parameters) {
		Query query = createQuery(queryCommand, parameters);

		return getSingleResult(query);
	}

	@SuppressWarnings("unchecked")
	protected List<T> getResultListByQuery(String queryCommand, List<Object> parameters) {
		Query query = createQuery(queryCommand, parameters);

		return query.getResultList();
	}


	@SuppressWarnings("unchecked")
	protected List<T> getResultListByQuery(String queryCommand,
			Map<String, Object> parameters) {
		Query query = createQuery(queryCommand, parameters);

		return query.getResultList();
	}

	/**
	 * Executa a Named Query de Update ou Delete.
	 * 
	 * @param namedQuery
	 * 		O nome da Named Query.
	 * @param parameters
	 * 		Os par�metros da instru��o.
	 */
	protected void executeNamedQuery(String namedQuery, Map<String, Object> parameters) {
		Query query = createNamedQuery(namedQuery, parameters);
		query.executeUpdate();
	}

	protected void executeQuery(String queryCommand, List<Object> parameters) {
		Query query = createQuery(queryCommand, parameters);
		query.executeUpdate();
	}

	/**
	 * @param queryCommand
	 * @param parameters
	 */
	protected void executeQuery(String queryCommand, Map<String, Object> parameters) {
		Query query = createQuery(queryCommand, parameters);
		query.executeUpdate();
	}

	/**
	 * Cria a Query a partir da Named Query.
	 * 
	 * @param namedQuery
	 * 		O nome da Named Query.
	 * @param parameters
	 * 		Os par�metros da instru��o. 
	 * @return A Query.
	 */
	private Query createNamedQuery(String namedQuery, Map<String, Object> parameters) {
		Query query = em.createNamedQuery(namedQuery);
		return initParameters(query, parameters);
	}


	/**
	 * 
	 * @param namedQuery
	 * @param parameters
	 * @return
	 */
	private Query createQuery(String queryCommand, List<Object> parameters) {
		Query query = em.createQuery(queryCommand);
		return initParameters(query, parameters);
	}

	/**
	 * 
	 * @param namedQuery
	 * @param parameters
	 * @return
	 */
	private Query createQuery(String queryCommand, Map<String, Object> parameters) {
		Query query = em.createQuery(queryCommand);
		return initParameters(query, parameters);
	}

	private Query initParameters(Query query, Map<String, Object> parameters) {
		if (parameters != null) {
			for (Entry<String, Object> mapEntry : parameters.entrySet()) {
				query.setParameter(mapEntry.getKey(), mapEntry.getValue());
			}
		}
		return query;
	}
	
	
	private Query initParameters(Query query, Object... parameters) {
		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				query.setParameter(i + 1, parameters[i]);
			}
		}
		return query;
	}

	private Query initParameters(Query query, List<Object> parameters) {
		if (parameters != null) {
			int i = 1;
			for (Object parametro : parameters) {
				query.setParameter(i++, parametro);
			}
		}
		return query;
	}


	public void flush() {
		em.flush();
	}

	public void refresh(T t) {
		em.refresh(t);
	}

}
