package com.company.controller;

import com.company.dto.*;
import com.company.dto.profile.ProfileDTO;
import com.company.service.AuthService;
import com.company.util.HttpHeaderUtil;
import com.company.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "Authorization and Registration")
@RestController
@RequestMapping("/auth")
public class AuthController {
    Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthService authService;
    @ApiOperation(value = "Login", notes = "Method for login", nickname = "reg_api")

     @PostMapping("/login")
    public ResponseEntity<ProfileDTO> login(@RequestBody AuthDTO dto) {
        logger.info("Request for loggin{}",dto);
        ProfileDTO profileDTO = authService.login(dto);
        return ResponseEntity.ok(profileDTO);
    }

    @ApiOperation(value = "Registration", notes = "Method for registration", nickname = "reg_api")
    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody RegistrationDTO dto) {
        String profileDTO = authService.registration(dto);
        return ResponseEntity.ok(profileDTO);
    }

    @ApiOperation(value = "phone verification", notes = "Method for verify phone", nickname = "log_api")
    @PostMapping("/phone/verification")
    public ResponseEntity<String> login(@RequestBody VerificationDTO dto) {
        String response = authService.verification(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/resend/{phone}")
    public ResponseEntity<ResponseInfoDTO> resendSms(@PathVariable("phone") String phone) {
        ResponseInfoDTO responseInfoDTO = authService.resendSms(phone);
        return ResponseEntity.ok(responseInfoDTO);
    }

    @ApiOperation(value = "Email verification", notes = "Method for verify email", nickname = "log_api")
    @GetMapping("/email/verification/{token}")
    public ResponseEntity<String> login(@PathVariable("token") String token) {
        Integer id = JwtUtil.decode(token);
        String response = authService.emailVerification(id);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Resend email", notes = "Method for email resending", nickname = "log_api")
    @GetMapping("/resend/email/{email}")
    public ResponseEntity<ResponseInfoDTO> resendEmail(@ApiParam(value = "email", readOnly = true, example = "your_email@gmail.com") @PathVariable("email") String email) {
        ResponseInfoDTO responseInfoDTO = authService.resendEmail(email);
        return ResponseEntity.ok(responseInfoDTO);
    }


}
