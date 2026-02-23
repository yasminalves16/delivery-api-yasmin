package com.deliverytech.delivery_api.service;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.deliverytech.delivery_api.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository repository;

    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
            com.deliverytech.delivery_api.model.User user =  repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

            return User.withUsername(user.getEmail())
            .password(user.getPassword())
            .build();
    } 

}
