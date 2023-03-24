package com.example.blogapi.service;

import com.example.blogapi.dto.response.CategoryDto;
import com.example.blogapi.entity.CategoryEntity;
import com.example.blogapi.repository.CategoryRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepo categoryRepo;

    public List<CategoryEntity> listCategoryActiveIsTrue() {
        return categoryRepo.listCategoryActive(true);
    }
    public List<CategoryDto> listCategoryActiveIsTrueAndChildren() {
        List<CategoryDto> categoryDtos = new ArrayList<>();
        List<CategoryEntity> categoryEntityListParent =  categoryRepo.listCategoryParentActive(true);
        if(!categoryEntityListParent.isEmpty()){
            for(CategoryEntity categoryEntity : categoryEntityListParent){
                CategoryDto categoryDto = new CategoryDto();

                List<CategoryDto> categoryDtosChild = new ArrayList<>();
                List<CategoryEntity> categoryEntityListChildren = categoryRepo.listCategoryHasParentActive(true, categoryEntity.getId());
                if(!categoryEntityListChildren.isEmpty()){
                    for(CategoryEntity categoryEntity1 : categoryEntityListChildren){
                        CategoryDto dto = new CategoryDto();
                        BeanUtils.copyProperties(categoryEntity1, dto);
                        categoryDtosChild.add(dto);
                    }
                }
                BeanUtils.copyProperties(categoryEntity, categoryDto);
                categoryDto.setChildrenCategory(categoryDtosChild);
                categoryDtos.add(categoryDto);

            }
        }
        return  categoryDtos;
    }

    public CategoryEntity addCategory(CategoryEntity category) {
        return categoryRepo.save(category);
    }
}
