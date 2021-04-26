package com.casestudy.service.cartItem;

import com.casestudy.model.CartItem;
import com.casestudy.model.Product;
import com.casestudy.model.User;
import com.casestudy.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ICartItemService extends IGeneralService<CartItem> {
    Iterable<CartItem> findAll();

    Iterable<CartItem> findAllByUser(User user);

    CartItem save(CartItem cartItem);

    Page<CartItem> findAll(Pageable pageable);

    Optional<CartItem> findById(Long id);

    Optional<Product> findByProductId(Long productId);

    void remove(Long id);

    CartItem findCartItem(User user, Product product);

    void deleteCartItemByCartItemId(Long id);

    void deleteCartItemByUser(User user);
}
