package com.example.blogapi.controller;

import com.example.blogapi.entity.CategoryEntity;
import com.example.blogapi.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<?> listAllCategory() {

        List<CategoryEntity> listCategoryActiveIsTrue = categoryService.listCategoryActiveIsTrue();

        if (listCategoryActiveIsTrue.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(listCategoryActiveIsTrue);
        }

    }
    @PostMapping
    public ResponseEntity<?> addCategory(@RequestBody CategoryEntity category) {

        CategoryEntity categorySaved = categoryService.addCategory(category);

        if (categorySaved == null) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(categorySaved);
        }

    }
}
