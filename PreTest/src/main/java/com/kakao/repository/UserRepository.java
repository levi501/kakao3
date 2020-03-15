package com.kakao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kakao.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByUserid(@Param("userid") String userid);
	
	User findByUserid(@Param("userid") String userid);
}
