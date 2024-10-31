package com.be.controller;

import com.be.config.JwtProvider;
import com.be.dto.request.LoginRequest;
import com.be.dto.response.AuthResponse;
import com.be.entity.User;
import com.be.repository.UserRepository;
import com.be.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//TODO USE MODEL MAPPER , LOGGER , GOOGLE AUTH
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
   private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;


    //signup
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(
            @RequestBody User user) throws Exception {
        User isEmailExist = userRepository.findByEmail(user.getEmail());

        String email = user.getEmail();
        String password = user.getPassword();
        String fullName = user.getFullName();
        if (isEmailExist!=null) {
            throw new Exception("Email Is Already Used With Another Account");
        }
        // Create new user
        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setFullName(fullName);
        createdUser.setPassword(passwordEncoder.encode(password));


        User savedUser = userRepository.save(createdUser);

        System.out.println("Registerd  User id "+savedUser.getId());

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //genrate jwt token
        String jwtToken = JwtProvider.generateToken(authentication);

        AuthResponse authResponse = AuthResponse.builder().jwt(jwtToken).status(true).message("Register Success").build();
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
    }

    //signin
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest loginRequest) {

        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        System.out.println(username + " ----- " + password);

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = JwtProvider.generateToken(authentication);
        AuthResponse authResponse = AuthResponse.builder()
                .jwt(jwtToken).status(true).message("Login Success").build();
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
    }



    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

        System.out.println("sign in userDetails - " + userDetails);

        if (userDetails == null) {
            System.out.println("sign in userDetails - null " + userDetails);
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            System.out.println("sign in userDetails - password not match " + userDetails);
            throw new BadCredentialsException("Invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
