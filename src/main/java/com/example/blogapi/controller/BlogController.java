package com.example.blogapi.controller;

import com.example.blogapi.dto.response.DetailPostResponse;
import com.example.blogapi.dto.response.ListRecentResponse;
import com.example.blogapi.dto.response.RecentDto;
import com.example.blogapi.entity.BlogEntity;
import com.example.blogapi.service.BlogService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/blogs")
public class BlogController {
    private final BlogService blogService;


    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping( )
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
    @GetMapping("/popular")
    public ResponseEntity<?> getPopular5Post(){
        List<RecentDto> recentDtoList = blogService.get5LimitRecentOrderByView();
        if (recentDtoList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(recentDtoList);
        }
    }

    @GetMapping("/posts/{id}/{page}")
    public ResponseEntity<?> getListPostByCategoryId(@PathVariable int id, @PathVariable int page){
        ListRecentResponse listDetailPostRes = blogService.getListDetailPostByCategoryId(id, page);
        if (listDetailPostRes.getRecentDtoList().isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(listDetailPostRes);
        }
    } @GetMapping("/search")
    public ResponseEntity<?> searchAll(@RequestParam String keyword){
//        ListRecentResponse listDetailPostRes = blogService.getListDetailPostByCategoryId(id, page);
//        if (listDetailPostRes.getRecentDtoList().isEmpty()) {
            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.ok().body(listDetailPostRes);
//        }
    }

//    @PostMapping( path = "/image",
//            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> uploadImageByAngularEditor(
//            @RequestPart("file") MultipartFile file
//    ) {
//        String imageUrl = file.getOriginalFilename();
//        Map map = new HashMap();
//        map.put("imageUrl" , imageUrl);
//        return ResponseEntity.ok(map);
//    }
}
