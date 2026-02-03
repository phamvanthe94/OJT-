package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface IUserStatisticRepository extends  JpaRepository<User, Long> {
    List<User> findByStatus(Boolean status);
}