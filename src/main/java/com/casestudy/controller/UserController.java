package com.casestudy.controller;

import java.security.Principal;
import java.util.*;

import com.casestudy.model.Product;
import com.casestudy.model.Role;
import com.casestudy.model.User;
import com.casestudy.service.cartItem.ICartItemService;
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
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    private ICartItemService cartItemService;

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
        ModelAndView modelAndView;
        if (name.isPresent()) {
            products = productService.findAllByNameContaining(name.get(), pageable);
            modelAndView = new ModelAndView("/customerView/home-product");
            modelAndView.addObject("products", products);
        } else {
            products = productService.findAll(pageable);
            modelAndView = new ModelAndView("/customerView/home");
            modelAndView.addObject("categories", categoryService.findAll());
        }
        return modelAndView;
    }

    @GetMapping("/user")
    public ModelAndView user(Principal principal, @RequestParam("q") Optional<String> name, @PageableDefault(size = 5) Pageable pageable) {
        // Get authenticated user name from Principal
        Page<Product> products;
        ModelAndView modelAndView;
        if (name.isPresent()) {
            products = productService.findAllByNameContaining(name.get(), pageable);
            modelAndView = new ModelAndView("/customerView/home-product");
            modelAndView.addObject("products", products);
        } else {
            products = productService.findAll(pageable);
            modelAndView = new ModelAndView("/customerView/home");
            modelAndView.addObject("categories", categoryService.findAll());
        }
        return modelAndView;
    }

    @GetMapping("/product-list")
    public ModelAndView showProductListForCustomer(@PageableDefault(size = 5) Pageable pageable) {
        Page<Product> products = productService.findAll(pageable);
        ModelAndView modelAndView = new ModelAndView("customerView/home-product");
        modelAndView.addObject("products", products);
//        if (principal != null) {
//            modelAndView.addObject("username", principal.getName());
//        }
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

    @GetMapping("/infomation/{username}")
    public ModelAndView showInfomationUser(@PathVariable String username){
        User user = userService.findByUsername(username);
        ModelAndView modelAndView = new ModelAndView("/customerView/infomation");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @PostMapping("/updateuser")
    public String updateInfomationUser(@ModelAttribute("user") User user){
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = new Role();
        role.setId(2);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        userService.save(user);
        return "redirect:/";
    }

    @GetMapping("/forgot")
    public ModelAndView showFormForgot(){
        User user = new User();
        ModelAndView modelAndView = new ModelAndView("/customerView/forgotpassword");
        modelAndView.addObject("userCheck",user);
        return modelAndView;
    }

    @PostMapping("/checkUserName")
    public ModelAndView checkUserName(@ModelAttribute("userCheck") User user){
        ModelAndView modelAndView = null;
        Iterable<User> all = userService.findAll();
        for (User u:all) {
            if (u.getUsername().equals(user.getUsername()) && u.getEmail().equals(user.getEmail())){
                modelAndView = new ModelAndView("/customerView/changepassword");
                User user1 = userService.findByUsername(user.getUsername());
                modelAndView.addObject("CheckUser",user1);
            }else {
                modelAndView = new ModelAndView("/customerView/login");
            }
        }
        return modelAndView;
    }

    @PostMapping("/NewPass")
    public String updateNewPassWord(@ModelAttribute("CheckUser") User user){
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = new Role();
        role.setId(2);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        userService.save(user);
        return "redirect:/";
    }

    @GetMapping("/payment")
    public ModelAndView showPaymentForm() {
        return new ModelAndView("customerView/checkout");
    }

    @PostMapping("/payment")
    public ModelAndView validatePayment(@RequestParam("card-num") String creditCardNumber, @RequestParam("username") String username) {
        boolean cardIsValid = userService.validatePayment(creditCardNumber);
        if (cardIsValid) {
            User user = userService.findByUsername(username);
            cartItemService.deleteCartItemByUser(user);
            return new ModelAndView("customerView/thank-you");
        }
        else {
            return new ModelAndView("customerView/invalid-payment");
        }
    }

    @GetMapping("/search-by-category/{categoryId}")
    public ModelAndView showProductByCategory(@PathVariable("categoryId") Long categoryId, @PageableDefault(size = 5) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("customerView/home-product");
        Page<Product> products = productService.findAllByCategory(categoryId, pageable);
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @GetMapping("/error-403")
    public ModelAndView showPage403(){
        ModelAndView modelAndView = new ModelAndView("/error-403");
        return modelAndView;
    }

}