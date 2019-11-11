package org.digam.users.entity;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.digam.users.boundary.UsersService;

@Singleton
@Startup
public class SeedData {
    
    @Inject
    private UsersService service;
    
    @PostConstruct
    public void init() {
        //Dummy accounts
        User adminUser = new User();
        adminUser.setCredential(new Credential("admin", "admin"));
        adminUser.setEmail("admin@digam.org");
        adminUser.setName("Admin");
        service.add(adminUser);
        
        User guestUser = new User();
        guestUser.setCredential(new Credential("guest", "guest"));
        guestUser.setEmail("guest@digam.org");
        guestUser.setName("Guest");
        
        service.add(guestUser);
    }
}
