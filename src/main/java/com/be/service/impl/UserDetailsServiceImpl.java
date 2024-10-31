package com.be.service.impl;


import com.be.entity.User;
import com.be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);

        if(user==null) {

            //TODO CHNAGE TO CUSTOM EXCEPTION HANDLING
            throw new UsernameNotFoundException("user not found with email  - "+username);
        }

//        String role=user.getRole();
//
        //TODO CHANGE NAME TO "USER"/"NORMALUSER"
//        if(role==null) role="CUSTOMER";

        List<GrantedAuthority> authorities=new ArrayList<>();

//        authorities.add(new SimpleGrantedAuthority(role));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),user.getPassword(),authorities);
    }
}
