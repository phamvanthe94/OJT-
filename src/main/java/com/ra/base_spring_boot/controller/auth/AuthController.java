package com.ra.base_spring_boot.controller.auth;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.req.authreq.FormLogin;
import com.ra.base_spring_boot.dto.req.authreq.FormRegister;
import com.ra.base_spring_boot.services.authsv.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;

    /**
     * @param formLogin FormLogin
     * @apiNote handle login with { email, password }
     */
    @PostMapping("/login")
    public ResponseEntity<?> handleLogin(@Valid @RequestBody FormLogin formLogin) {
        return ResponseEntity.ok().body(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .code(200)
                        .data(authService.login(formLogin))
                        .build()
        );
    }

    /**
     * @param formRegister FormRegister
     * @apiNote handle register with { firstName, lastName, email, password, phone, address, avatar }
     */
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> handleRegister(@Valid @ModelAttribute FormRegister formRegister) {
        authService.register(formRegister);
        return ResponseEntity.created(URI.create("api/v1/auth/register")).body(
                ResponseWrapper.builder()
                        .status(HttpStatus.CREATED)
                        .code(201)
                        .data("Register successfully")
                        .build()
        );
    }

}
