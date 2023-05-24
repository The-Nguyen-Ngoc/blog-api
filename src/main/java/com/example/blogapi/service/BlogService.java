package com.example.blogapi.service;

import com.example.blogapi.config.EmailConfig;
import com.example.blogapi.dto.response.DetailPostResponse;
import com.example.blogapi.dto.response.ListRecentResponse;
import com.example.blogapi.dto.response.RecentDto;
import com.example.blogapi.entity.BlogEntity;
import com.example.blogapi.repository.BlogRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.MailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BlogService {
    private final BlogRepo blogRepo;

    private final MailSender mailSender;

    public BlogService(BlogRepo blogRepo,  MailSender mailSender) {
        this.blogRepo = blogRepo;
        this.mailSender = mailSender;
    }
    public BlogEntity addBlog(BlogEntity blog) {
        return blogRepo.save(blog);
    }
//    @Scheduled(fixedRate = 10000)
//    public void printLine() {
//        System.out.println("This line will be printed every 10 seconds.");
//    }
    public ListRecentResponse getAllRecent(Map map) {
        ListRecentResponse listRecentResponse = new ListRecentResponse();
        int page = map.get("page") != null ? (Integer.parseInt(map.get("page").toString())) - 1 : 0;
        int pageSize = map.get("pageSize") != null ? (Integer.parseInt(map.get("pageSize").toString()))  : 5;
        Pageable pageable = PageRequest.of(page , pageSize, Sort.by("dateCreate").descending());
        Page<BlogEntity> blogEntityPage = blogRepo.findAll(pageable);
        List<BlogEntity> blogEntities = blogEntityPage.getContent();
        List<RecentDto> recentDtos = new ArrayList<>();

        if (!blogEntities.isEmpty()) {
            blogEntities.forEach(item -> {
                RecentDto recentDto = new RecentDto();
                BeanUtils.copyProperties(item, recentDto);
                recentDtos.add(recentDto);
            });
        }
        listRecentResponse.setRecentDtoList(recentDtos);
        listRecentResponse.setTotalPages(blogEntityPage.getTotalPages());
        listRecentResponse.setTotalElement(blogEntityPage.getTotalElements());
        return listRecentResponse;
    }

    public List<RecentDto> get5LimitRecentOrderByView(){
        List<BlogEntity> blogEntities = blogRepo.findFirst5ByOrderByViewDesc();
        List<RecentDto> recentDtos = new ArrayList<>();
        for ( BlogEntity blog : blogEntities){
            RecentDto recentDto = new RecentDto();
            BeanUtils.copyProperties(blog, recentDto);
            recentDtos.add(recentDto);
        }
        return recentDtos;
    }
    public List<RecentDto> searchByKeyword(String keyword){
        List<BlogEntity> blogEntities = blogRepo.findBlogEntitiesByKeyword(keyword);
        List<RecentDto> recentDtos = new ArrayList<>();
        for ( BlogEntity blog : blogEntities){
            RecentDto recentDto = new RecentDto();
            BeanUtils.copyProperties(blog, recentDto);
            recentDtos.add(recentDto);
        }
        return recentDtos;
    }

    public DetailPostResponse getDetailPostById(int id) {
        BlogEntity blogEntity = blogRepo.findById(id);
        DetailPostResponse detailPostResponse = new DetailPostResponse();

        if(blogEntity != null) {
            blogEntity.setView(blogEntity.getView()+ 1);
            blogRepo.save(blogEntity);
            BeanUtils.copyProperties(blogEntity,detailPostResponse);
        }

        return detailPostResponse;
    }
    public ListRecentResponse getListDetailPostByCategoryId(int id, int page) {
        ListRecentResponse listRecentResponse = new ListRecentResponse();
        Pageable pageable = PageRequest.of(page-1 , 10, Sort.by("dateCreate").descending());
        Page<BlogEntity> listBlogEntity = blogRepo.findByCategoryId(id ,pageable);
        List<RecentDto> listRecent = new ArrayList<>();

        if(!listBlogEntity.getContent().isEmpty()) {
            for (BlogEntity blog : listBlogEntity.getContent()){
                RecentDto recentDto = new RecentDto();
                BeanUtils.copyProperties(blog, recentDto);
                listRecent.add(recentDto);
            }
        }
        listRecentResponse.setRecentDtoList(listRecent);
        listRecentResponse.setTotalPages(listBlogEntity.getTotalPages());
        listRecentResponse.setTotalElement(listBlogEntity.getTotalElements());
        return listRecentResponse;
    }
}
