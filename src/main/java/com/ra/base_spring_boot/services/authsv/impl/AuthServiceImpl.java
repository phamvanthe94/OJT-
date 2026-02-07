package com.ra.base_spring_boot.services.authsv.impl;

import com.ra.base_spring_boot.dto.req.authreq.FormLogin;
import com.ra.base_spring_boot.dto.req.authreq.FormRegister;
import com.ra.base_spring_boot.dto.resp.JwtResponse;
import com.ra.base_spring_boot.exception.HttpBadRequest;
import com.ra.base_spring_boot.model.constants.RoleName;
import com.ra.base_spring_boot.model.constants.UserStatus;
import com.ra.base_spring_boot.model.entity.user.Role;
import com.ra.base_spring_boot.model.entity.user.User;
import com.ra.base_spring_boot.repository.authrp.IUserRepository;
import com.ra.base_spring_boot.security.jwt.JwtProvider;
import com.ra.base_spring_boot.security.principle.MyUserDetails;
import com.ra.base_spring_boot.services.impl.CloudinaryService;
import com.ra.base_spring_boot.services.authsv.IAuthService;
import com.ra.base_spring_boot.services.authsv.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final IRoleService roleService;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final CloudinaryService cloudinaryService;

    @Override
    public void register(FormRegister formRegister) {
        if (userRepository.existsByEmail((formRegister.getEmail()))) {
            throw new HttpBadRequest("Email đã được sử dụng");
        }
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.findByRoleName(RoleName.ROLE_USER));

        String avatarUrl = null;
        if (formRegister.getAvatar() != null && !formRegister.getAvatar().isEmpty()) {
            avatarUrl = cloudinaryService.upload(formRegister.getAvatar());
        }
        User user = User.builder()
                .firstName(formRegister.getFirstName())
                .lastName(formRegister.getLastName())
                .email(formRegister.getEmail())
                .password(passwordEncoder.encode(formRegister.getPassword()))
                .phone(formRegister.getPhone())
                .address(formRegister.getAddress())
                .avatar(avatarUrl)
                .status(UserStatus.ACTIVE)
                .roles(roles)
                .build();
        userRepository.save(user);
    }

    @Override
    public JwtResponse login(FormLogin formLogin) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(formLogin.getEmail(), formLogin.getPassword()));
        } catch (AuthenticationException e) {
            throw new HttpBadRequest("Username or password is incorrect");
        }

        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        if (userDetails.getUser().getStatus() == UserStatus.BLOCKED) {
            throw new HttpBadRequest("your account is blocked");
        }


        return JwtResponse.builder()
                .accessToken(jwtProvider.generateToken(userDetails.getUsername()))
                .user(userDetails.getUser())
                .roles(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
                .build();
    }


}
