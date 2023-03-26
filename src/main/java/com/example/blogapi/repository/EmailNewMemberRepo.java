package com.example.blogapi.repository;

import com.example.blogapi.entity.EmailNewMember;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailNewMemberRepo extends CrudRepository<EmailNewMember, Long> {
    EmailNewMember findEmailNewMemberByIp(String ip);
}
