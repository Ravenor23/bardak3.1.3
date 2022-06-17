package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {

    public User findById(Long id);

    public List<User> findAll();

    public User saveUser(User user);

    public void deleteById(Long id);

    public UserDetails loadUserByUsername(String string);

    public User findByUsername(String username);
}
