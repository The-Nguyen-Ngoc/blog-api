package com.example.blogapi.service;

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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogService {
    private final BlogRepo blogRepo;

    public BlogService(BlogRepo blogRepo) {
        this.blogRepo = blogRepo;
    }

    public BlogEntity addBlog(BlogEntity blog) {
        return blogRepo.save(blog);
    }

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

    public DetailPostResponse getDetailPostById(int id) {
        BlogEntity blogEntity = blogRepo.findById(id);
        DetailPostResponse detailPostResponse = new DetailPostResponse();

        if(blogEntity != null) {
            BeanUtils.copyProperties(blogEntity,detailPostResponse);
        }

        return detailPostResponse;
    }
    public List<DetailPostResponse> getListDetailPostByCategoryId(int id) {
        List<BlogEntity> listBlogEntity = blogRepo.findByCategoryId(id);
        List<DetailPostResponse> listDetailPostResponse = new ArrayList<>();

        if(!listBlogEntity.isEmpty()) {
            for (BlogEntity blog : listBlogEntity){
                DetailPostResponse detailPostResponse = new DetailPostResponse();
                BeanUtils.copyProperties(blog, detailPostResponse);
                listDetailPostResponse.add(detailPostResponse);
            }
        }
        return listDetailPostResponse;
    }
}
