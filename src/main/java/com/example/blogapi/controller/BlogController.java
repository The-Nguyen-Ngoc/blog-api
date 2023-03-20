package com.example.blogapi.controller;

import com.example.blogapi.dto.response.DetailPostResponse;
import com.example.blogapi.dto.response.ListRecentResponse;
import com.example.blogapi.dto.response.RecentDto;
import com.example.blogapi.entity.BlogEntity;
import com.example.blogapi.service.BlogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/blogs")
public class BlogController {
    private final BlogService blogService;


    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping()
    public ResponseEntity<?> addBlog(@RequestBody BlogEntity blogEntity){
        BlogEntity blogSaved = blogService.addBlog(blogEntity);
        if (blogSaved == null) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(blogSaved);
        }
    }

    @GetMapping("/recents")
    public ResponseEntity<?> getRecents(@RequestParam Map map){
        System.out.println(map);
        ListRecentResponse listRecentResponse = blogService.getAllRecent(map);
        if (listRecentResponse.getRecentDtoList().isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(listRecentResponse);
        }
    }

    @GetMapping("/detail-post/{id}")
    public ResponseEntity<?> getDetailPost(@PathVariable int id){
        DetailPostResponse detailPostById = blogService.getDetailPostById(id);
        if (detailPostById == null) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(detailPostById);
        }
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<?> getListPostByCategoryId(@PathVariable int id){
        List<DetailPostResponse> listDetailPostRes = blogService.getListDetailPostByCategoryId(id);
        if (listDetailPostRes.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(listDetailPostRes);
        }
    }
}
