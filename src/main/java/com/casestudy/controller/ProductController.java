package com.casestudy.controller;

import com.casestudy.model.Category;
import com.casestudy.model.Product;
import com.casestudy.model.ProductForm;
import com.casestudy.service.category.ICategoryService;
import com.casestudy.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/admin/products")
public class ProductController {

    @Value(value = "${upload.path}")
    private String fileUpload;

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/list")
    public ModelAndView showAll(@RequestParam("q") Optional<String> name, @PageableDefault(size = 5) Pageable pageable) {
        Page<Product> products;
        if (name.isPresent()) {
            products = productService.findAllByNameContaining(name.get(), pageable);
        } else {
            products = productService.findAll(pageable);
        }
        Iterable<Category> categories = categoryService.findAll();
        ModelAndView modelAndView = new ModelAndView("/adminView-product/list");
        modelAndView.addObject("products", products);
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("adminView-product/create");
        Iterable<Category> categories = categoryService.findAll();
        modelAndView.addObject("categories", categories);
        modelAndView.addObject("product", new ProductForm()); // what does this line do?
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView createProduct(@ModelAttribute ProductForm productForm) {
        Product product = new Product();
        MultipartFile multipartFile = productForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(productForm.getImage().getBytes(), new File(this.fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        product.setName(productForm.getName());
        product.setPrice(productForm.getPrice());
        product.setDescription(productForm.getDescription());
        product.setCategory(productForm.getCategory());
        product.setImgUrl(fileName);
        productService.save(product);
        Iterable<Category> categories = categoryService.findAll();
        ModelAndView modelAndView = new ModelAndView("adminView-product/create");
        modelAndView.addObject("product", new ProductForm());
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showUpdateForm(@PathVariable Long id) {
        Optional<Product> productOptional = productService.findById(id);
        Iterable<Category> categories = categoryService.findAll();
        ModelAndView modelAndView;
        if (productOptional.isPresent()) {
            modelAndView = new ModelAndView("adminView-product/update");
            Product product = productOptional.get();
            ProductForm productForm = new ProductForm(product.getId(), product.getName(), product.getPrice(), product.getDescription(), null, product.getCategory());
            modelAndView.addObject("product", productForm);
            modelAndView.addObject("categories", categories);
        } else {
            modelAndView = new ModelAndView("error-404");
        }
        return modelAndView;
    }

    @PostMapping("/update")
    public String updateProduct(@ModelAttribute ProductForm productForm) {
        Product product = new Product();
        MultipartFile multipartFile = productForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(productForm.getImage().getBytes(), new File(this.fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        product.setId(productForm.getId());
        product.setName(productForm.getName());
        product.setPrice(productForm.getPrice());
        product.setDescription(productForm.getDescription());
        product.setImgUrl(fileName);
        product.setCategory(productForm.getCategory());
        productService.save(product);
        return "redirect:/admin/products/list";
    }

    @GetMapping("/delete/{id}")
    public ModelAndView showDeleteConfirmation(@PathVariable Long id) {
        Optional<Product> product = productService.findById(id);
        ModelAndView modelAndView;
        if (product.isPresent()) {
            modelAndView = new ModelAndView("/adminView-product/delete");
            modelAndView.addObject("product", product);
        } else {
            modelAndView = new ModelAndView("error-404");
        }
        return modelAndView;
    }

    @PostMapping("/delete")
    public String deleteProduct(@ModelAttribute Product product) {
        productService.remove(product.getId());
        return "redirect:/admin/products/list";
    }
}
