package de.th.wildau.im14.was.service;

import java.io.Serializable;
import java.security.Principal;
import java.util.logging.Logger;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.FetchParent;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import de.th.wildau.im14.was.model.BaseEntity;
import de.th.wildau.im14.was.model.User;

public abstract class AbstractService<T extends BaseEntity<T>> implements
		Serializable {

	private static final long serialVersionUID = 5199886820301686035L;

	@Inject
	protected Logger log;

	@Inject
	protected Principal principal;

	@Inject
	protected EntityManager em;

	@Inject
	private Event<T> eventSrc;

	// private Class<T> classType;

	@Deprecated
	protected CriteriaQuery<T> getCriteriaQuery() {
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		// return cb.createQuery(classType.getClass());
		return null;
	}

	@Deprecated
	protected Root<T> getRoot() {
		// return getCriteriaQuery().from(T);
		return null;
	}

	protected User getCurrentUser() {
		if (this.principal == null
				|| StringUtils.isEmpty(this.principal.getName())
				|| this.principal.getName() == "anonymous") {
			return null;
		}
		// CriteriaBuilder cb = this.em.getCriteriaBuilder();
		// CriteriaQuery<User> criteria = cb.createQuery(User.class);
		// Root<User> user = criteria.from(User.class);
		// // FIXME to Lower/Upper case
		// ParameterExpression<String> p = cb.parameter(String.class,
		// this.principal.getName());
		// criteria.select(user).where(cb.equal(user.get("email"), p));
		//
		// TypedQuery<User> query = this.em.createQuery(criteria);
		// query.setMaxResults(1);
		// List<User> result = query.getResultList();
		// if (result.isEmpty()) {
		// return null;
		// }
		// return result.get(0);

		final String qstring = "SELECT u FROM User u WHERE u.email = :param";
		TypedQuery<User> query = em.createQuery(qstring, User.class);
		query.setParameter("param", this.principal.getName());
		return query.getSingleResult();
	}

	// public T find(final Long primaryKeyId, final String fetchRelations) {
	// return this.em.find(this.classType, primaryKeyId);
	// }
	//
	// public List<T> findAll(final String fetchRelations) {
	// CriteriaBuilder cb = this.em.getCriteriaBuilder();
	// CriteriaQuery<T> criteria = cb.createQuery(this.classType);
	// Root<T> types = criteria.from(this.classType);
	//
	// //addFetches(types, fetchRelations.split(" "));
	// // sample: "addresses friends.addresses"
	//
	// criteria.select(types);// .orderBy(cb.asc(member.get("name")));
	// // criteriaQuery.where(criteriaBuilder.equal(types.get("id"), id))
	// return this.em.createQuery(criteria).getResultList();
	// }

	protected static <T> void addFetches(Root<T> root, String... fetchRelations) {
		for (String relation : fetchRelations) {
			FetchParent<T, T> fetch = root;
			for (String pathSegment : relation.split(".")) {
				fetch = fetch.fetch(pathSegment, JoinType.LEFT);
			}
		}
	}

	// public T getSingleOrNoneResult(final Long primaryKeyId) {
	// CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
	// CriteriaQuery<T> criteriaQuery = criteriaBuilder
	// .createQuery(this.classType);
	// criteriaQuery.where(criteriaBuilder.equal(
	// criteriaQuery.from(this.classType).get("id"), primaryKeyId));
	//
	// TypedQuery<T> query = em.createQuery(criteriaQuery);
	// query.setMaxResults(1);
	// List<T> result = query.getResultList();
	// if (result.isEmpty()) {
	// return null;
	// }
	// return result.get(0);
	// }

	protected void delete(final T entity) {
		this.log.info("delete: " + entity);
		this.em.remove(entity);
		this.eventSrc.fire(entity);
	}

	protected T merge(final T entity) {
		this.log.info("update: " + entity);
		T result = this.em.merge(entity);
		this.eventSrc.fire(entity);
		return result;
	}

	protected void create(final T entity) {
		this.log.info("create: " + entity);
		this.em.persist(entity);
		this.eventSrc.fire(entity);
	}
}
