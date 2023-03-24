package com.example.blogapi.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class CategoryDto {
    private int id;
    private String name;
    private List<CategoryDto> childrenCategory;
}
