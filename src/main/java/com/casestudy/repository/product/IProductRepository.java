package com.casestudy.repository.product;

import com.casestudy.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByNameContaining(String name, Pageable pageable);

    @Query("select p from Product p where p.category.id=:categoryId")
    Page<Product> findAllByCategory(Long categoryId, Pageable pageable);
}
