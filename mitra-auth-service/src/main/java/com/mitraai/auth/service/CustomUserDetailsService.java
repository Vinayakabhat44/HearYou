package com.mitraai.auth.service;

import com.mitraai.auth.entity.User;
import com.mitraai.auth.repository.UserRepository;
import com.mitraai.auth.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrMobileNumber(identifier, identifier)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with identifier: " + identifier));

        return new CustomUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole())));
    }
}
