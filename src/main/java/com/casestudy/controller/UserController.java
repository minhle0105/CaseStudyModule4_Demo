package com.casestudy.controller;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.casestudy.model.User;
import com.casestudy.service.category.ICategoryService;
import com.casestudy.service.product.IProductService;
import com.casestudy.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/customerView/home");
        modelAndView.addObject("products", productService.findAll());
        modelAndView.addObject("categories", categoryService.findAll());
        return modelAndView;
    }

    @GetMapping("/user")
    public ModelAndView user(Principal principal) {
        // Get authenticated user name from Principal
        ModelAndView modelAndView = new ModelAndView("/customerView/home");
        modelAndView.addObject("products", productService.findAll());
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
    public ModelAndView admin() {
        // Get authenticated user name from SecurityContext
        SecurityContext context = SecurityContextHolder.getContext();
        System.out.println(context.getAuthentication().getName());
        ModelAndView modelAndView = new ModelAndView("/adminView-product/list");
        modelAndView.addObject("products", productService.findAll());
        return modelAndView;
    }

    @GetMapping("/signup")
    public ModelAndView showSignUpForm(){
        ModelAndView modelAndView = new ModelAndView("/customerView/signup");
        modelAndView.addObject("newUser", new User());
        return modelAndView;
    }

    @PostMapping("/signup")
    public ModelAndView SignUp(@Valid @ModelAttribute("newUser") User user, BindingResult bindingResult){
        user.validate(user,bindingResult, (List<User>) userService.findAll());
        Set<User> i = new HashSet<>();

        if (bindingResult.hasFieldErrors()){
            return new ModelAndView("/customerView/signup");
        }
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setRoles();
        userService.save(user);
        return new ModelAndView("/customerView/login");
    }
}