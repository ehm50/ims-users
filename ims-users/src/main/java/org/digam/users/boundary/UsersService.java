package org.digam.users.boundary;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.digam.users.entity.User;

@Stateless
public class UsersService {

	@PersistenceContext
	private EntityManager em;

	public Set<User> getAll() {
		List<User> list = em.createQuery("FROM User u", User.class).getResultList();
		return new LinkedHashSet(list);
	}

	public Optional<User> get(Long id) {
		User found = em.find(User.class, id);
		return found != null ? Optional.of(found) : Optional.empty();
	}

	public void add(User newUser) {
		em.persist(newUser);
	}

	public boolean update(User updated) {
		User found = em.find(User.class, updated.getId());
		if (found != null) {
			em.merge(updated);
			return true;
		}
		return false;
	}

	public void remove(Long id) {
		Query query = em.createQuery("DELETE FROM User u WHERE u.id = :id");
		query.setParameter("id", id).executeUpdate();
	}

}
