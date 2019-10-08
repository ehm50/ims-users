package org.digam.users.entity;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
@Startup
public class SeedData {

	@PersistenceContext
	private EntityManager em;

	@PostConstruct
	public void init() {
		// Dummy data to begin with
		User adminUser = new User("Admin", "admin@gmx.ch");
		em.persist(adminUser);
		User guestUser = new User("Guest", "guest@gmx.ch");
		em.persist(guestUser);
	}
}
