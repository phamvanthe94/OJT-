package com.ra.base_spring_boot.services;

import com.ra.base_spring_boot.model.entity.user.Role;
import com.ra.base_spring_boot.model.constants.RoleName;

public interface IRoleService
{
    Role findByRoleName(RoleName roleName);
}
