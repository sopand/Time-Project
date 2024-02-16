package com.time.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.time.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{

}
