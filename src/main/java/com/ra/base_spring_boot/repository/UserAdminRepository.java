package com.ra.base_spring_boot.repository;


import com.ra.base_spring_boot.model.entity.user.User;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface UserAdminRepository extends JpaRepository<User, Long> {

    @Query("""
        SELECT u
        FROM User u
        WHERE (:keyword IS NULL OR :keyword = '' 
            OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
    """)
    Page<User> searchUsers(@Param("keyword") String keyword, Pageable pageable);
}