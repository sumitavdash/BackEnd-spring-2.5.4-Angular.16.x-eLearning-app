package com.ExamPort.ExamServer.services.impl;

import com.ExamPort.ExamServer.Entities.User;
import com.ExamPort.ExamServer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserSecurityServiceImp implements UserDetailsService {


    @Autowired

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        User user=this.userRepository.findByUsername(username);

       if(user==null)
       {
           System.out.println("User Not Found");
           throw new UsernameNotFoundException("No User Found God!");
       }
        return user;
    }
}
