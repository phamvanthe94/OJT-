package com.ra.base_spring_boot.security.Initializer;

import com.ra.base_spring_boot.model.constants.RoleName;
import com.ra.base_spring_boot.repository.authrp.IRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final IRoleRepository roleRepository;

    @Override
    public void run(String... args) {
        Arrays.asList(RoleName.ROLE_USER, RoleName.ROLE_ADMIN).forEach(this::createRoleIfNotExists);
    }


    public void createRoleIfNotExists(RoleName roleName) {
        if (!roleRepository.existsByRoleName(roleName)) {
            roleRepository.save(
                    com.ra.base_spring_boot.model.entity.user.Role.builder()
                            .roleName(roleName)
                            .build()
            );
        }
    }
}
