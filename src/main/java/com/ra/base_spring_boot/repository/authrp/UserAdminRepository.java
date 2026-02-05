package com.ra.base_spring_boot.repository.authrp;

import com.ra.base_spring_boot.model.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserAdminRepository extends JpaRepository<User, Long> {

    @Query("""
                SELECT u
                FROM User u
                WHERE (:keyword IS NULL OR :keyword = ''
                    OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :keyword, '%'))
                )
            """)
    Page<User> searchUsers(@Param("keyword") String keyword, Pageable pageable);
}
