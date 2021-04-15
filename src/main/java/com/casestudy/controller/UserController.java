package com.casestudy.controller;

import java.security.Principal;
import java.util.Optional;

import com.casestudy.model.Product;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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

    @GetMapping("/")
    public ModelAndView index(@RequestParam("q") Optional<String> name, @PageableDefault(size = 5) Pageable pageable) {
        Page<Product> products;
        if (name.isPresent()) {
            products = productService.findAllByNameContaining(name.get(), pageable);
        }
        else {
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
        }
        else {
            products = productService.findAll(pageable);
        }
        ModelAndView modelAndView = new ModelAndView("/customerView/home");
        modelAndView.addObject("products", products);
        modelAndView.addObject("categories", categoryService.findAll());
        System.out.println(principal.getName());
        return modelAndView;
    }

    @GetMapping("/login")
    public String login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken){
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
        }
        else {
            products = productService.findAll(pageable);
        }
        ModelAndView modelAndView = new ModelAndView("/adminView-product/list");
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @GetMapping("/signup")
    public ModelAndView showSignUpForm(){
        ModelAndView modelAndView = new ModelAndView("/customerView/signup");
        modelAndView.addObject("newUser", new User());
        return modelAndView;
    }

    @PostMapping("/signup")
    public String SignUp(@ModelAttribute User user){
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return "redirect:/login";
    }
}