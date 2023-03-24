package com.example.blogapi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tb_category")
@Data
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private int parentId;
    private boolean status = true;
}
