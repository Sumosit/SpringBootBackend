package com.example.pegieback.controller;

import com.example.pegieback.message.ResponseMessage;
import com.example.pegieback.models.Role;
import com.example.pegieback.models.User;
import com.example.pegieback.repository.RoleRepository;
import com.example.pegieback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UsersController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @DeleteMapping("/api/users/delete")
    public void deleteUser(@RequestParam Long id) {
        userRepository.delete(new User(id, null, null));
    }

    @PostMapping("/api/users/add/role")
    public ResponseEntity<User> addUser(@RequestBody ResponseMessage responseMessage) {
        User user = userRepository.findUserById(responseMessage.id);
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findRoleByName(responseMessage.message));
        user.setRole(roles);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(user);
    }

    @PostMapping("/api/users/add")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findRoleByName("ROLE_USER"));
        return ResponseEntity.status(HttpStatus.OK)
                .body(userRepository.save(new User(null, user.getUsername(), roles)));
    }

    @GetMapping("/api/users/get/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userRepository.findAll());
    }

    @GetMapping("/api/roles/all")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(roleRepository.findAll());
    }

    // Для heroku
    @GetMapping("/api/roles/add")
    public void addRole() {
//        if (role == 1) {
            roleRepository.save(new Role(null, "ROLE_USER"));
//        } else if (role == 2) {
            roleRepository.save(new Role(null, "ROLE_MODERATOR"));
//        } else if (role == 3) {
            roleRepository.save(new Role(null, "ROLE_BANNED"));
//        }
    }

    @GetMapping("/api/users/getAll/byRole")
    public ResponseEntity<List<User>> getUsersByRole(@RequestParam String role) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userRepository.findUsersByRole(roleRepository.findRoleByName(role)));
    }

}
