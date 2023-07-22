package com.example.demo.repository;

import com.example.demo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LuceneRepostory extends JpaRepository<Member, Long> {
}
