package com.example.blogapi.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class DetailPostResponse {
    private int id;
    private String title;
    private String header;
    private String body;
    private Date dateCreate;
    private long view;
    private int categoryId;
    private String userCreate;
    private String imgPreview;
}
