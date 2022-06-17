package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleDao;
import ru.kata.spring.boot_security.demo.repository.UserDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @PersistenceContext
    private EntityManager em;
    private final UserDao userDao;

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
        User updatedUser = findByUsername(user.getUsername());
        if (updatedUser == null) {
            return userDao.save(user);
        }
        updatedUser.setPassword(user.getPassword());
        updatedUser.setUsername(user.getUsername());
        updatedUser.setAge(user.getAge());
        updatedUser.setLastname(user.getLastname());
        updatedUser.setName(user.getName());
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


       /* Set<GrantedAuthority> grantedAuthorities= new HashSet<>();
        for  (Role role : user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }*/
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthorities(user));
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(User user) {
        String[] userRoles = user.getRoles().stream().map((role) -> role.getName()).toArray(String[]::new);
        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userRoles);
        return authorities;
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
