package org.sid.secservice.sec.service;

import org.sid.secservice.sec.entities.AppRole;
import org.sid.secservice.sec.entities.AppUser;
import org.sid.secservice.sec.repositories.AppRoleRepository;
import org.sid.secservice.sec.repositories.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    private AppUserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AppRoleRepository roleRepository;

    public AccountServiceImpl(AppUserRepository userRepository, PasswordEncoder passwordEncoder, AppRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public AppUser addNewUser(AppUser appUser) {
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return userRepository.save(appUser);
    }

    @Override
    public AppRole addNewRole(AppRole appRole) {
        return roleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String username, String rolename) throws Exception {
        AppUser appUser = userRepository.findByUserName(username);
        AppRole role = roleRepository.findByRoleName(rolename);
        if (appUser == null || role == null) throw new Exception();
        appUser.getAppRoles().add(role);
    }

    @Override
    public AppUser loadUserByUserName(String username) {
        return userRepository.findByUserName(username);
    }

    @Override
    public List<AppUser> listUsers() {
        return userRepository.findAll();
    }
}
