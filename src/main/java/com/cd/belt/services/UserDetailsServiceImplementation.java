package com.cd.belt.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cd.belt.models.Role;
import com.cd.belt.models.User;
import com.cd.belt.repositories.UserRepo;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

    private UserRepo userRepo;
    
    public UserDetailsServiceImplementation(UserRepo userRepo){
    this.userRepo = userRepo;
    }

    // 1 Finds the user by their username. If a user is found, it returns it with the correct authorities. If the username does not exist, the method throws an error.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);
        
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthorities(user));
    }
    
    // 2 returns a list of authorities/permissions for a specific user. 
    private List<GrantedAuthority> getAuthorities(User user){
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for(Role role : user.getRoles()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getName());
            authorities.add(grantedAuthority);
        }
        return authorities;
    }
}