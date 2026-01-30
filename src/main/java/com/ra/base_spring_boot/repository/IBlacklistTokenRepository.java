package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.entity.user.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface IBlacklistTokenRepository extends JpaRepository<BlacklistedToken, String> {
    @Modifying
    @Transactional
    @Query("DELETE FROM BlacklistedToken b WHERE b.expiredAt <: now")
    void deleteExpired(@Param("now") Date now);
}
