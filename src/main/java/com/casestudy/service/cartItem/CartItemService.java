package com.casestudy.service.cartItem;

import com.casestudy.model.CartItem;
import com.casestudy.model.Product;
import com.casestudy.model.User;
import com.casestudy.repository.cartItem.ICartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartItemService implements ICartItemService {

    @Autowired
    private ICartItemRepository cartItemRepository;

    @Override
    public Iterable<CartItem> findAll() {
        return cartItemRepository.findAll();
    }

    @Override
    public Iterable<CartItem> findAllByUser(User user) {
        return cartItemRepository.findAllByUser(user);
    }

    @Override
    public CartItem save(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    @Override
    public Page<CartItem> findAll(Pageable pageable) {
        return cartItemRepository.findAll(pageable);
    }

    @Override
    public Optional<CartItem> findById(Long id) {
        return cartItemRepository.findById(id);
    }

    @Override
    public Optional<Product> findByProductId(Long productId) {
        return cartItemRepository.findByProductId(productId);
    }

    @Override
    public void remove(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public CartItem findQuantity(User user, Product product) {
        return cartItemRepository.findQuantity(user, product);
    }

}
