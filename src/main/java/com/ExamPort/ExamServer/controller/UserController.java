package com.ExamPort.ExamServer.controller;


import com.ExamPort.ExamServer.Entities.Role;
import com.ExamPort.ExamServer.Entities.User;
import com.ExamPort.ExamServer.Entities.UserRole;
import com.ExamPort.ExamServer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
//@CrossOrigin("*")
public class UserController {



    final private  UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //creating user
    @PostMapping("/")
    public User createUser(@RequestBody User user) throws Exception {
        user.setProfile("default.png");

        // password encode with Bcrypt
        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));


        Set<UserRole> newRoles = new HashSet<>();
        Role role = new Role();
        role.setRoleId(23L);
        role.setRoleName("General USER");

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);

        newRoles.add(userRole);

        return this.userService.createUser(user, newRoles);

    }
    //finding  User by Id
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

//get all user
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }


    //delete the user by id
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId")Long userId){

        this.userService.deleteUser(userId);
    }
  //update the user

//update user data by userid
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long userId,
            @RequestBody User updatedUser
    ) {
        Optional<User> optionalUser = Optional.ofNullable(userService.getUserById(userId));

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(updatedUser.getUsername());
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setPhone(updatedUser.getPhone());
            user.setEmail(updatedUser.getEmail());
            user.setProfile(updatedUser.getProfile());

            User savedUser = userService.saveUser(user);
            return ResponseEntity.ok(savedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    //for password reset all api starts from here
    // Initiate password reset
    @PostMapping("/initiate-password-reset")
    public ResponseEntity<String> initiatePasswordReset(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            userService.initiatePasswordReset(email);
            return ResponseEntity.ok("Password reset initiated. Check your email for further instructions.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error initiating password reset: " + e.getMessage());
        }
    }

    // Complete password reset
    @PostMapping("/complete-password-reset")
    public ResponseEntity<String> completePasswordReset(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            String newPassword = request.get("newPassword");
            userService.completePasswordReset(token, newPassword);
            return ResponseEntity.ok("Password reset successfully completed.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error completing password reset: " + e.getMessage());
        }
    }

    @GetMapping("/byEmail")

    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        User user = userService.findByEmail(email);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @GetMapping("/byResetToken")
    public ResponseEntity<User> getUserByResetToken(@RequestParam String resetToken) {
        User user = userService.findByResetToken(resetToken);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    //end here



}
