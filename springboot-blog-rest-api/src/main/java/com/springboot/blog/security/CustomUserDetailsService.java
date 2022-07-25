package com.springboot.blog.security;

import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service //("CustomUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
       User user = userRepository.findByUserNameOrEmail(usernameOrEmail, usernameOrEmail)  // Attention here User is blog.entity.User, not security.core.userdetails.User
                .orElseThrow(()->
                        new UsernameNotFoundException("User not found by username or Email"+ usernameOrEmail));

                return new org.springframework.security.core.userdetails
                        .User(user.getEmail(), user.getPassword(), mapRoleToAuthorities(user.getRoles()));
    }
    // First we pass the rule to User object, but User object has Set<Role>
    // so we need to convert the Set<Role> to set up (Collection) granting Authority(授權)
    private Collection<? extends GrantedAuthority> mapRoleToAuthorities(Set<Role> roles){
        // use Stream api to convert Set<Role> to Collection grantedAuthority
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

}
