package com.casestudy.repository.cartItem;

import com.casestudy.model.CartItem;
import com.casestudy.model.Product;
import com.casestudy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT ci FROM CartItem ci WHERE ci.user = :user")
    Iterable<CartItem> findAllByUser(@Param("user") User user);

    @Query("SELECT p from Product p where p.id = :productId")
    Optional<Product> findByProductId(@Param("productId") Long productId);

    @Query("SELECT ci from CartItem ci where ci.product=:product and ci.user=:user")
    CartItem findCartItem(@Param("user") User user, @Param("product") Product product);

    @Modifying
    @Query("DELETE from CartItem ci where ci.id=:cartItemId")
    void deleteCartItemByUser(@Param("cartItemId") Long cartItemId);
}
