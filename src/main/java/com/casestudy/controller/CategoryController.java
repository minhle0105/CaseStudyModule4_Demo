package com.casestudy.controller;

import com.casestudy.model.Category;
import com.casestudy.model.CategoryForm;
import com.casestudy.service.category.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Value(value = "${upload.path}")
    private String fileUpload;

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/list")
    public ModelAndView showAll() {
        Iterable<Category> categories = categoryService.findAll();
        ModelAndView modelAndView = new ModelAndView("adminView-category/list");
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("adminView-category/create");
        modelAndView.addObject("category", new Category());
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView createProduct(@ModelAttribute CategoryForm categoryForm) {
        Category category = new Category();
        MultipartFile multipartFile = categoryForm.getImageLink();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(categoryForm.getImageLink().getBytes(), new File(this.fileUpload + fileName));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        category.setName(categoryForm.getName());
        category.setImageLink(fileName);
        categoryService.save(category);
        ModelAndView modelAndView = new ModelAndView("adminView-category/create");
        modelAndView.addObject("category", new CategoryForm());
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showUpdateForm(@PathVariable Long id) {
        Optional<Category> categoryOptional = categoryService.findById(id);
        ModelAndView modelAndView;
        if (categoryOptional.isPresent()) {
            modelAndView = new ModelAndView("adminView-category/update");
            modelAndView.addObject("category", categoryOptional.get());
        }
        else {
            modelAndView = new ModelAndView("error-404");
        }
        return modelAndView;
    }

    @PostMapping("/update")
    public String updateProduct(@ModelAttribute Category category) {
        categoryService.save(category);
        return "redirect:/categories/list";
    }

    @GetMapping("/delete/{id}")
    public ModelAndView showDeleteConfirmation(@PathVariable Long id) {
        Optional<Category> category = categoryService.findById(id);
        ModelAndView modelAndView;
        if (category.isPresent()) {
            modelAndView = new ModelAndView("/adminView-category/delete");
            modelAndView.addObject("category", category);
        }
        else {
            modelAndView = new ModelAndView("error-404");
        }
        return modelAndView;
    }

    @PostMapping("/delete")
    public String deleteProduct(@ModelAttribute Category category) {
        categoryService.remove(category.getId());
        return "redirect:/categories/list";
    }
}
