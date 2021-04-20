package com.casestudy.controller;

import com.casestudy.model.CartItem;
import com.casestudy.model.Product;
import com.casestudy.model.User;
import com.casestudy.service.cartItem.ICartItemService;
import com.casestudy.service.user.IUserService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartItemController {

    @Autowired
    private IUserService userService;

    @Autowired
    private ICartItemService cartItemService;

    @ModelAttribute("user")
    public User getCurrentUser(Principal principal) {
        return userService.findByUsername(principal.getName());
    }


    @GetMapping("/show/{username}")
    public ModelAndView showCart(@PathVariable("username") String username) {
        User user = userService.findByUsername(username);
        Iterable<CartItem> cartItems = cartItemService.findAllByUser(user);
        ModelAndView modelAndView = new ModelAndView("customerView/cart");
        modelAndView.addObject("cartItems", cartItems);
        return modelAndView;

    }

    @GetMapping("/add/{username}/{productId}")
    public String addItem(@PathVariable("username") String username, @PathVariable("productId") Long productId) {
        User user = userService.findByUsername(username);
        CartItem cartItem = new CartItem();
        Optional<Product> product = cartItemService.findByProductId(productId);
        if (product.isPresent()) {
            cartItem.setUser(user);
            cartItem.setProduct(product.get());
            CartItem existedCartItem = cartItemService.findQuantity(user, product.get());
            if (existedCartItem == null) {
                cartItem.setQuantity(1);
            }
            else {
                cartItem.setId(existedCartItem.getId());
                cartItem.setQuantity(existedCartItem.getQuantity()+1);
            }
            cartItemService.save(cartItem);
        }
        else {
            return null;
        }
        return "redirect:/";
    }

    @GetMapping("/update/{userId}")
    public ModelAndView updateItem(@RequestParam("userId") Long userId) {
        return null;
    }

    @GetMapping("/delete/{userId}")
    public ModelAndView deleteItem(@RequestParam("userId") Long userId) {
        return null;
    }
}
