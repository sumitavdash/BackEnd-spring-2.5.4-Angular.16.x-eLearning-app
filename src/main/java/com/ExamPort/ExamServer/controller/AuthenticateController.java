package com.ExamPort.ExamServer.controller;

import com.ExamPort.ExamServer.Entities.JwtRequest;
import com.ExamPort.ExamServer.Entities.JwtResponse;
import com.ExamPort.ExamServer.Entities.User;
import com.ExamPort.ExamServer.configure.JwtUtils;
import com.ExamPort.ExamServer.services.impl.UserSecurityServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
//@CrossOrigin("*")
public class AuthenticateController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserSecurityServiceImp userSecurityServiceImp;

    @Autowired
    private JwtUtils jwtUtils;

    // Endpoint for generating a JWT token
    @PostMapping("/generate-token")
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        try {
            // Authenticate the user credentials
            authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
        } catch (UsernameNotFoundException e) {
            // User not found
            e.printStackTrace();
            throw new Exception("User Not Found");
        }

        // If authenticated, load the user details and generate the token
        UserDetails userDetails = this.userSecurityServiceImp.loadUserByUsername(jwtRequest.getUsername());
        String token = this.jwtUtils.generateToken(userDetails);

        // Return the JWT token in the response body
        return ResponseEntity.ok(new JwtResponse(token));
    }

    // Helper method to authenticate user credentials
    private void authenticate(String username, String password) throws Exception {
        try {
             authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
          //  SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (DisabledException e) {
            // User is disabled
            throw new Exception("User Is Disabled: " + e.getMessage());
        } catch (BadCredentialsException e) {
            // Invalid credentials
            throw new Exception("Invalid Credentials: " + e.getMessage());
        }
    }

    //returns the details of current user
    @GetMapping("/current-user")
    public User getCurrentUser(Principal principal){

        return ((User) this.userSecurityServiceImp.loadUserByUsername(principal.getName()));
    }
}
