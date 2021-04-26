package com.casestudy.service.product;

import com.casestudy.model.Product;
import com.casestudy.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService extends IGeneralService<Product> {

    Page<Product> findAllByNameContaining(String name, Pageable pageable);

    Page<Product> findAllByCategory(Long categoryId, Pageable pageable);
}
