package com.example.blogapi.service;

import com.example.blogapi.entity.CategoryEntity;
import com.example.blogapi.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepo categoryRepo;

    public List<CategoryEntity> listCategoryActiveIsTrue() {
        return categoryRepo.listCategoryActive(true);
    }

    public CategoryEntity addCategory(CategoryEntity category) {
        return categoryRepo.save(category);
    }
}
