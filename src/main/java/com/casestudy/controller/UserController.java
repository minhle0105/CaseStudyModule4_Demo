package com.casestudy.controller;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.casestudy.model.Product;
import com.casestudy.model.Role;
import com.casestudy.model.User;
import com.casestudy.service.category.ICategoryService;
import com.casestudy.service.product.IProductService;
import com.casestudy.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ModelAttribute("username")
    public String getUsername(Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            return principal.getName();

        }
        return "";
    }

    @GetMapping("/")
    public ModelAndView index(@RequestParam("q") Optional<String> name, @PageableDefault(size = 5) Pageable pageable) {
        Page<Product> products;
        if (name.isPresent()) {
            products = productService.findAllByNameContaining(name.get(), pageable);
        } else {
            products = productService.findAll(pageable);
        }
        ModelAndView modelAndView = new ModelAndView("/customerView/home");
        modelAndView.addObject("products", products);
        modelAndView.addObject("categories", categoryService.findAll());
        return modelAndView;
    }

    @GetMapping("/user")
    public ModelAndView user(Principal principal, @RequestParam("q") Optional<String> name, @PageableDefault(size = 5) Pageable pageable) {
        // Get authenticated user name from Principal
        Page<Product> products;
        if (name.isPresent()) {
            products = productService.findAllByNameContaining(name.get(), pageable);
        } else {
            products = productService.findAll(pageable);
        }
        ModelAndView modelAndView = new ModelAndView("/customerView/home");
        modelAndView.addObject("products", products);
        modelAndView.addObject("categories", categoryService.findAll());
        modelAndView.addObject("username", principal.getName());
        System.out.println(principal.getName());
        return modelAndView;
    }

    @GetMapping("/login")
    public String login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "/customerView/login";
        }
        return "redirect:/";
    }

    @GetMapping("/admin")
    public ModelAndView admin(@RequestParam("q") Optional<String> name, @PageableDefault(size = 5) Pageable pageable) {
        // Get authenticated user name from SecurityContext
        SecurityContext context = SecurityContextHolder.getContext();
        System.out.println(context.getAuthentication().getName());
        Page<Product> products;
        if (name.isPresent()) {
            products = productService.findAllByNameContaining(name.get(), pageable);
        } else {
            products = productService.findAll(pageable);
        }
        ModelAndView modelAndView = new ModelAndView("/adminView-product/list");
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @GetMapping("/signup")
    public ModelAndView showSignUpForm() {
        ModelAndView modelAndView = new ModelAndView("/customerView/signup");
        modelAndView.addObject("newUser", new User());
        return modelAndView;
    }

    @PostMapping("/signup")
    public ModelAndView SignUp(@Valid @ModelAttribute("newUser") User user, BindingResult bindingResult) {
        user.validate(user, bindingResult, (List<User>) userService.findAll());
        if (bindingResult.hasFieldErrors()) {
            return new ModelAndView("/customerView/signup");
        }
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = new Role();
        role.setId(2);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        userService.save(user);
        return new ModelAndView("/customerView/login");
    }
}