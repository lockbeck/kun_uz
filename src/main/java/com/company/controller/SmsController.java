package com.company.controller;

import com.company.enums.ProfileRole;
import com.company.service.EmailService;
import com.company.service.SmsService;
import com.company.util.HttpHeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/sms")
public class SmsController {
    @Autowired
    private SmsService smsService;
    @Autowired
    private EmailService emailService;
    @GetMapping("/pagination")
    public ResponseEntity<PageImpl> getPaginationOfSms(@RequestParam(value = "page", defaultValue = "1") int page,
                                                  @RequestParam(value = "size", defaultValue = "5") int size,
                                                  HttpServletRequest request) {
        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        PageImpl response = smsService.pagination(page, size);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/email/pagination")
    public ResponseEntity<PageImpl> getPaginationOfEmail(@RequestParam(value = "page", defaultValue = "1") int page,
                                                  @RequestParam(value = "size", defaultValue = "5") int size,
                                                  HttpServletRequest request) {
        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        PageImpl response = emailService.pagination(page, size);
        return ResponseEntity.ok().body(response);
    }
}
