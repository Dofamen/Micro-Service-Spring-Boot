package org.sid.secservice.sec.service;

import org.sid.secservice.sec.entities.AppRole;
import org.sid.secservice.sec.entities.AppUser;

import java.util.List;

public interface AccountService {
    public AppUser addNewUser(AppUser appUser);
    public AppRole addNewRole(AppRole appRole);
    public void addRoleToUser(String username, String rolename) throws Exception;
    public AppUser loadUserByUserName(String username);
    public List<AppUser> listUsers();
}
