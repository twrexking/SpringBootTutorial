package com.example.demo.repository;

import com.example.demo.model.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends MongoRepository<Member, String> {
    Member findByUsername(String username);
}