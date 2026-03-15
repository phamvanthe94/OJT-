package com.ra.base_spring_boot.services.authsv;

import com.ra.base_spring_boot.dto.req.ChangeUserStatusRequest;
import com.ra.base_spring_boot.dto.resp.UserAdminResponse;
import org.springframework.data.domain.Page;

public interface IUserAdminService {

    Page<UserAdminResponse> getAllUsers(String keyword, int page, int size, String sortBy, String direction);

    UserAdminResponse changeUserStatus(Long id, ChangeUserStatusRequest request);
}
