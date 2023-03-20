package com.example.blogapi.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "tb_blog")
@Data
public class BlogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String header;
    private String body;
    private Date dateCreate = new Date();
    private long view;
    private int categoryId;
    private String userCreate;
    private String imgPreview;
    private boolean status = true;
}
