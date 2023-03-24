package com.example.blogapi.repository;

import com.example.blogapi.dto.response.RecentDto;
import com.example.blogapi.entity.BlogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepo extends PagingAndSortingRepository<BlogEntity, Integer> {
    BlogEntity save(BlogEntity blog);

    @Query("select t from  BlogEntity t where t.id = ?1 and t.status=true")
    BlogEntity findById(int id);
    @Query("select t from  BlogEntity t where t.categoryId = ?1 and t.status=true")
    Page<BlogEntity> findByCategoryId(int id, Pageable pageable);

    List<BlogEntity> findFirst5ByOrderByViewDesc();
}
