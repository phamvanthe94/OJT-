package com.ra.base_spring_boot.services.authsv;

public interface ITokenBlacklistCleanupService {
    void cleanUpExpiredTokens();
}
