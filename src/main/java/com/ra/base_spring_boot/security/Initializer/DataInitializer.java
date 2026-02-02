package com.ra.base_spring_boot.security.Initializer;

import com.ra.base_spring_boot.model.constants.RoleName;
import com.ra.base_spring_boot.repository.IRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final IRoleRepository roleRepository;

    /**
     * Initialize default roles in the database if they do not exist.
     */
    @Override
    public void run(String... args) {
        Arrays.asList(RoleName.ROLE_USER, RoleName.ROLE_ADMIN).forEach(this::createRoleIfNotExists);
    }


    /**
     * Create a role in the database if it does not already exist.
     *
     * @param roleName the name of the role to create
     */
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
