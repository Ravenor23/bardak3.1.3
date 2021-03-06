package ru.kata.spring.boot_security.demo.controller;

import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;
    private User admin;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "users/login";
    }

    @GetMapping("/user")
    public String showUserInfo(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        model.addAttribute("user", user);
        return "users/show";
    }

    @GetMapping("/admin/user")
    public String showAdminInfo(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        model.addAttribute("user", user);
        return "users/adminShow";
    }

    @GetMapping("/admin/user/{id}")
    public String showUserById(@PathVariable("id") Long id, Model model) {
        model.addAttribute(userService.findById(id));
        return "users/show";
    }

    @GetMapping("/admin")
    public String findAll(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        admin = userService.findByUsername(auth.getName());
        List<User> users = userService.findAll();
        User user = new User();
        model.addAttribute("users", users);
        model.addAttribute("admin", admin);
        return "users/index";
    }

    @PostMapping("/admin/user-update/")
    public String updateUserForm(@ModelAttribute("user") User user, @RequestParam(value="roles") List<Role> roles) {
        userService.updateUser(user, user.getId());
        return "redirect:/admin";
    }

    /*@PostMapping("/admin/user-update")
    public String updateUser(User user, Model model) {
        model.addAttribute("user", user);
        userService.saveUser(user);
        return "redirect:/admin";
    }*/

    @GetMapping("/admin/user-create")
    public String createUserForm(User user, Model model) {

        model.addAttribute("admin", admin);
        return "users/saveUser";
    }

    @PostMapping("/admin/user-create")
    public String createUser(User user, @RequestParam(value = "roles") List<Role> roles) {
        userService.saveUser(user);
        return "redirect:/admin/";
    }

    @PostMapping("/admin/user-delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin/";
    }

}
