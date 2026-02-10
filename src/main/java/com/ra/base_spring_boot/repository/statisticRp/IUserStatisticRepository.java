package com.ra.base_spring_boot.repository.statisticRp;

import com.ra.base_spring_boot.model.constants.UserStatus;
import com.ra.base_spring_boot.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface IUserStatisticRepository extends JpaRepository<User, Long> {
    @Query("""
    SELECT 
        COUNT(u),
        SUM(CASE WHEN u.status = 'ACTIVE' THEN 1 ELSE 0 END),
        SUM(CASE WHEN u.status = 'BLOCKED' THEN 1 ELSE 0 END)
    FROM User u
""")
    List<Object[]> statisticUser();


    List<User> findByStatus(UserStatus status);
}