/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kltn.motelservice.service;

import com.kltn.motelservice.entity.Role;
import com.kltn.motelservice.entity.RoleName;
import com.kltn.motelservice.entity.User;
import com.kltn.motelservice.model.UserDTO;
import com.kltn.motelservice.repository.RoleRepository;
import com.kltn.motelservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User selectUserByEmail(String email) {
        return userRepository.findByEmail(email).get();
    }

    @Override
    public Role selectRoleByName(RoleName name) {
        return roleRepository.findByName(name);
    }

    @Override
    public User changePassword(Long id, String oldPassword, String newPassword) throws Exception {
        User user = selectUserById(id);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) throw new Exception();
        user.setPassword(passwordEncoder.encode(newPassword));

        return userRepository.save(user);
    }

    @Override
    public Page<User> selectPageOfUsers(Pageable page) {
        return userRepository.findAll(page);
    }

    @Override
    public User blockUserById(Long id, boolean block) {
        User user = selectUserById(id);
        user.setBlock(block);
        return userRepository.save(user);
    }

    @Override
    public User selectUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User changeProfile(UserDTO userDTO) {
        User user = selectUserById(userDTO.getId());
        user.setPhone(userDTO.getPhone());
        user.setFullName(userDTO.getFullName());
        user.setAddress(userDTO.getAddress());

        return userRepository.save(user);
    }


}
