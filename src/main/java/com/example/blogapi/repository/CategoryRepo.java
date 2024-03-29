package com.example.blogapi.repository;

import com.example.blogapi.entity.CategoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends CrudRepository<CategoryEntity, Integer> {
    @Query("SELECT c FROM CategoryEntity c where c.status = ?1 and c.parentId = 0")
    List<CategoryEntity> listCategoryParentActive(boolean status);
    @Query("SELECT c FROM CategoryEntity c where c.status = ?1")
    List<CategoryEntity> listCategoryActive(boolean status);

    @Query("SELECT c FROM CategoryEntity c where c.status = ?1 and c.parentId = ?2")
    List<CategoryEntity> listCategoryHasParentActive(boolean status, int parentId);
}
