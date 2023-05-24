package com.example.blogapi.controller;

import com.example.blogapi.dto.request.EmailDetails;
import com.example.blogapi.dto.response.DetailPostResponse;
import com.example.blogapi.dto.response.ListRecentResponse;
import com.example.blogapi.dto.response.RecentDto;
import com.example.blogapi.entity.BlogEntity;
import com.example.blogapi.repository.EmailNewMemberRepo;
import com.example.blogapi.service.BlogService;

import com.example.blogapi.service.MyProducer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/blogs")
public class BlogController {
    private static final Logger logger = LogManager.getLogger(BlogController.class);

    private final BlogService blogService;
    private final JavaMailSender mailSender;
    private final EmailNewMemberRepo emailNewMemberRepo;

    private static final String TOPIC = "emails";

    @Autowired
    private MyProducer kafkaTemplate;

    public BlogController(BlogService blogService, JavaMailSender mailSender, EmailNewMemberRepo emailNewMemberRepo) {
        this.blogService = blogService;
        this.mailSender = mailSender;
        this.emailNewMemberRepo = emailNewMemberRepo;
    }

    @PostMapping()
    public ResponseEntity<?> addBlog(@RequestBody BlogEntity blogEntity) {
        BlogEntity blogSaved = blogService.addBlog(blogEntity);
        if (blogSaved == null) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(blogSaved);
        }
    }

    @GetMapping("/recents")
    public ResponseEntity<?> getRecents(@RequestParam Map map) {
        logger.info("---------------------------------GET LIST RECENTS-----------------------------------");
        logger.info("Recent project API: {}", map);
        ListRecentResponse listRecentResponse = blogService.getAllRecent(map);
        if (listRecentResponse.getRecentDtoList().isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(listRecentResponse);
        }
    }

    @GetMapping("/detail-post/{id}")
    public ResponseEntity<?> getDetailPost(@PathVariable int id) {
        logger.info("---------------------------------GET DETAIL POST BY ID-----------------------------------");

        DetailPostResponse detailPostById = blogService.getDetailPostById(id);
        if (detailPostById == null) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(detailPostById);
        }
    }

    @GetMapping("/popular")
    public ResponseEntity<?> getPopular5Post() {
        logger.info("---------------------------------GET POPULAR-----------------------------------");

        List<RecentDto> recentDtoList = blogService.get5LimitRecentOrderByView();
        if (recentDtoList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(recentDtoList);
        }
    }

    @GetMapping("/posts/{id}/{page}")
    public ResponseEntity<?> getListPostByCategoryId(@PathVariable int id, @PathVariable int page) {
        logger.info("---------------------------------GET POSTS BY PAGE-----------------------------------");
        logger.info("REQUEST: " +"PAGE: "+ page +" ID: "+ id);


        ListRecentResponse listDetailPostRes = blogService.getListDetailPostByCategoryId(id, page);
        if (listDetailPostRes.getRecentDtoList().isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(listDetailPostRes);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchAll(@RequestParam String keyword) {
        logger.info("---------------------------------SEARCH -----------------------------------");
        logger.info("REQUEST: " +keyword);

        List<RecentDto> recentDtoList = blogService.searchByKeyword(keyword);
        if (recentDtoList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(recentDtoList);
        }
    }

    @PostMapping("/email")
    public ResponseEntity<?> submitEmail(@RequestBody EmailDetails email) {
        logger.info("---------------------------------SEND EMAIL NEW MEMBER-----------------------------------");
        logger.info("REQUEST: " +email);

        try {
            if (email != null) {
                kafkaTemplate.sendMessage("emails", email.getEmailTo());


            }
            return new ResponseEntity<>(email, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
