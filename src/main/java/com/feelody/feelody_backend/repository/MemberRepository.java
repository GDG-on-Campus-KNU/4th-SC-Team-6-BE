package com.feelody.feelody_backend.repository;

import com.feelody.feelody_backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Long> {

}
