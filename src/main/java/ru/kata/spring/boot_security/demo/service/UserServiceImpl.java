package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleDao;
import ru.kata.spring.boot_security.demo.repository.UserDao;

import java.util.*;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserDao userDao;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private final RoleDao roleDao;

    @Autowired
    public UserServiceImpl(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    @Override
    public User findById(Long id) {
        return userDao.findById(id).orElse(null);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));

        return userDao.save(user);
    }

    @Override
    public User updateUser(User user, Long id) {
        User updatedUser = findById(id);
        updatedUser.setName(user.getName());
        updatedUser.setLastname(user.getLastname());
        updatedUser.setAge(user.getAge());
        updatedUser.setUsername(user.getUsername());
        updatedUser.setPassword(encoder.encode(user.getPassword()));
        updatedUser.setRoles(user.getRoles());
        return userDao.save(updatedUser);
    }


    @Override
    public void deleteById(Long id) {
        userDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthorities(user));
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(User user) {
        String[] userRoles = user.getRoles().stream().map((role) -> role.getName()).toArray(String[]::new);
        return AuthorityUtils.createAuthorityList(userRoles);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
