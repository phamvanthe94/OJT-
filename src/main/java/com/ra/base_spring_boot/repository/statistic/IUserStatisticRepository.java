package com.ra.base_spring_boot.repository.statistic;

import com.ra.base_spring_boot.model.constants.UserStatus;
import com.ra.base_spring_boot.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IUserStatisticRepository extends JpaRepository<User, Long> {
    long countByStatus(UserStatus status);
}