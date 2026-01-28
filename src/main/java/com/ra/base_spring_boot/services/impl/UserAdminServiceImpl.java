package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.req.ChangeUserStatusRequest;
import com.ra.base_spring_boot.dto.resp.UserAdminResponse;
import com.ra.base_spring_boot.model.entity.user.User;
import com.ra.base_spring_boot.repository.UserAdminRepository;
import com.ra.base_spring_boot.services.IUserAdminService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class UserAdminServiceImpl implements IUserAdminService {

    private final UserAdminRepository userAdminRepo;

    public UserAdminServiceImpl(UserAdminRepository userAdminRepo) {
        this.userAdminRepo = userAdminRepo;
    }

    @Override
    public Page<UserAdminResponse> getAllUsers(String keyword, int page, int size, String sortBy, String direction) {

        String sortField = (sortBy == null || sortBy.isBlank()) ? "id" : sortBy;

        Sort sort = Sort.by(sortField);
        if ("desc".equalsIgnoreCase(direction)) sort = sort.descending();
        else sort = sort.ascending();

        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), sort);

        Page<User> result = userAdminRepo.searchUsers(keyword == null ? "" : keyword.trim(), pageable);

        return result.map(u -> UserAdminResponse.builder()
                .id(u.getId())
                .fullName(u.getFullName())
                .username(u.getUsername())
                .status(u.getStatus())
                .roles(
                        u.getRoles().stream()
                                .map(r -> r.getRoleName().name())
                                .collect(Collectors.toSet())
                )
                .build());
    }

    @Override
    @Transactional
    public UserAdminResponse changeUserStatus(Long id, ChangeUserStatusRequest request) {

        User user = userAdminRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));

        user.setStatus(request.getStatus());
        User saved = userAdminRepo.save(user);

        return UserAdminResponse.builder()
                .id(saved.getId())
                .fullName(saved.getFullName())
                .username(saved.getUsername())
                .status(saved.getStatus())
                .roles(
                        saved.getRoles().stream()
                                .map(r -> r.getRoleName().name())
                                .collect(Collectors.toSet())
                )
                .build();
    }
}