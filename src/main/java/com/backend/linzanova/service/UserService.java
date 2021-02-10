package com.backend.linzanova.service;

import com.backend.linzanova.dao.IUserDao;
import com.backend.linzanova.dto.OrderItemsDTO;
import com.backend.linzanova.dto.UserOrderDTO;
import com.backend.linzanova.dto.UserPageDTO;
import com.backend.linzanova.dto.UserPasswordRenewDTO;
import com.backend.linzanova.entity.user.User;
import com.backend.linzanova.exeption.DoesNotExistException;
import com.backend.linzanova.exeption.NoRightsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService implements UserDetailsService, IUserService {

    @Autowired
    private IUserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userDao.findByEmail(email);
    }

    @Override
    public String insertUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (!user.getRole().startsWith("ROLE_")) {
            throw new RuntimeException("User role should start with 'ROLE_'");
        }
        User savedUser = userDao.save(user);
        return savedUser.getUsername();
    }

    @Override
    public UserPageDTO getAllUsers(Pageable pageable) {
        final Page<User> all = userDao.findAll(pageable);
        return new UserPageDTO(all.getContent(), all.getTotalElements(), all.getSize(), all.isEmpty(), all.getTotalPages());
    }

    @Override
    public User getUser(int id, String username) {
        User user = userDao.findById(id).orElseThrow(() -> new DoesNotExistException("Користувача із id: " + id + "не знайдено"));
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (user.getUsername().equals(username) || userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
            return user;
        }else {
            throw new NoRightsException("No rights for user: " + username);
        }
    }

    @Override
    public User updateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDao.save(user);
    }

    @Override
    public Long totalCount(){
        return userDao.countUsers();
    }

    @Override
    public UserPageDTO getAllUsersByName(Pageable pageable, String name) {
        final  Page<User> byFirstNameContains = userDao.findByFirstNameContains(pageable, name);
        return new UserPageDTO(byFirstNameContains.getContent(), byFirstNameContains.getTotalElements(), byFirstNameContains.getSize(), byFirstNameContains.isEmpty(), byFirstNameContains.getTotalPages());
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public User getUserById(int id) {
        return userDao.findById(id).orElseThrow(() -> new DoesNotExistException("Користувача із id: " + id + "не знайдено"));
    }
}
