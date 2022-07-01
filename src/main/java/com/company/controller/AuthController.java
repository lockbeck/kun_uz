package com.company.controller;

import com.company.dto.*;
import com.company.dto.profile.ProfileDTO;
import com.company.service.AuthService;
import com.company.util.HttpHeaderUtil;
import com.company.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@Api(tags = "Authorization and Registration")
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;



    @ApiOperation(value = "Login", notes = "Method for login", nickname = "login_api")
    @PostMapping("/login")
    public ResponseEntity<ProfileDTO> login(@RequestBody @Valid AuthDTO dto) {
        log.info("Request for login{}",dto);
        ProfileDTO profileDTO = authService.login(dto);
        return ResponseEntity.ok(profileDTO);
    }


    @ApiOperation(value = "Registration", notes = "Method for registration")
    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody @Valid RegistrationDTO dto) {
        log.info("Request for registration{}",dto);
        String profileDTO = authService.registration(dto);
        return ResponseEntity.ok(profileDTO);
    }


    @ApiOperation(value = "phone verification", notes = "Method for verify phone")
    @PostMapping("/phone/verification")
    public ResponseEntity<String> login(@RequestBody VerificationDTO dto) {
        log.info("Request for phone verification{}",dto);
        String response = authService.verification(dto);
        return ResponseEntity.ok(response);
    }


    @ApiOperation(value = "Resend phone", notes = "Method for resend phone")
    @GetMapping("/resend/{phone}")
    public ResponseEntity<ResponseInfoDTO> resendSms(@ApiParam(value = "phone", required = true, example = "998909123456")
                                                         @PathVariable("phone") String phone) {
        log.info("Request for resend phone{}",phone);
        ResponseInfoDTO responseInfoDTO = authService.resendSms(phone);
        return ResponseEntity.ok(responseInfoDTO);
    }


    @ApiOperation(value = "Email verification", notes = "Method for verify email")
    @GetMapping("/email/verification/{token}")
    public ResponseEntity<String> login(@ApiParam(value = "token", required = true, example = "token of user")
                                            @PathVariable("token") String token) {
        log.info("Request for email verification{}",token);
        Integer id = JwtUtil.decode(token);
        String response = authService.emailVerification(id);
        return ResponseEntity.ok(response);
    }


    @ApiOperation(value = "Resend email", notes = "Method for email resending")
    @GetMapping("/resend/email/{email}")
    public ResponseEntity<ResponseInfoDTO> resendEmail(@ApiParam(value = "email", readOnly = true, example = "your_email@gmail.com")
                                                           @PathVariable("email")
//                                                                   @Valid @Pattern(regexp = "^(.+)@(.+)$")
                                                                   String email) {

        log.info("Request for resend email {}",email);
        ResponseInfoDTO responseInfoDTO = authService.resendEmail(email);
        return ResponseEntity.ok(responseInfoDTO);
    }


}
