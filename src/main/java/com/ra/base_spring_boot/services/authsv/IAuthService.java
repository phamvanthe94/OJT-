package com.ra.base_spring_boot.services.authsv;

import com.ra.base_spring_boot.dto.req.authreq.FormLogin;
import com.ra.base_spring_boot.dto.req.authreq.FormRegister;
import com.ra.base_spring_boot.dto.resp.authresp.JwtResponse;

public interface IAuthService {

    void register(FormRegister formRegister);

    JwtResponse login(FormLogin formLogin);

    void logout(String token);

}
