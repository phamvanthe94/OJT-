package com.ra.base_spring_boot.services.authsv.impl;

import com.ra.base_spring_boot.repository.authrp.IBlacklistTokenRepository;
import com.ra.base_spring_boot.services.authsv.ITokenBlacklistCleanupService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenBlacklistCleanupServiceImpl implements ITokenBlacklistCleanupService {

    private final IBlacklistTokenRepository blacklistTokenRepository;

    @Scheduled(cron = "0 0 * * * *")
    @Override
    public void cleanUpExpiredTokens() {
        blacklistTokenRepository.deleteExpired(new Date());
    }
}
