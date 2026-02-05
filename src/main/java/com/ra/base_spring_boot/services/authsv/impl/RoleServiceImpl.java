package com.ra.base_spring_boot.services.authsv.impl;

import com.ra.base_spring_boot.exception.HttpNotFound;
import com.ra.base_spring_boot.model.constants.RoleName;
import com.ra.base_spring_boot.model.entity.user.Role;
import com.ra.base_spring_boot.repository.authrp.IRoleRepository;
import com.ra.base_spring_boot.services.authsv.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {
    private final IRoleRepository roleRepository;

    @Override
    public Role findByRoleName(RoleName roleName) {
        return roleRepository.findByRoleName(roleName).orElseThrow(() -> new HttpNotFound("role not found"));
    }
}
