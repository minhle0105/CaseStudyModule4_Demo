package com.casestudy.service.category;

import com.casestudy.model.Category;
import com.casestudy.repository.category.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    public Iterable<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public void remove(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Iterable<Category> findByName(String name) {
        Iterable<Category> categories = findAll();
        ArrayList<Category> results = new ArrayList<>();
        for (Category c : categories) {
            if (c.getName().toLowerCase().contains(name.toLowerCase())) {
                results.add(c);
            }
        }
        return results;
    }
}
