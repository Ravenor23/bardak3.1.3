package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    public String showUserInfo(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        model.addAttribute("user", user);
        return "users/show";
    }

    @GetMapping("/admin/user/{id}")
    public String showUserById(@PathVariable("id") Long id, Model model) {
        model.addAttribute(userService.findById(id));
        return "users/show";
    }

    @GetMapping("/admin")
    public String findAll(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "users/index";
    }

    @GetMapping("/admin/user-update/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "users/edit";
    }

    @PostMapping("/admin/user-update")
    public String updateUser(User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/user-create")
    public String createUserForm(User user) {
        return "users/saveUser";
    }

    @PostMapping("/admin/user-create")
    public String createUser(User user) {
        userService.saveUser(user);
        return "redirect:/admin/";
    }

    @GetMapping("/admin/user-delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin/";
    }
}
