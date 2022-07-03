package com.company.controller;

import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.exp.BadRequestException;
import com.company.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/init")
@Slf4j
public class InitController {

    @Autowired
    private ProfileRepository profileRepository;


    @GetMapping("/initAdmin")
    public ResponseEntity<?> initAdmin() {
        Optional<ProfileEntity> optional = profileRepository.findByEmail("admin@gmail.com");
        if (optional.isPresent()) {
            throw new BadRequestException("Email already registered");
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName("Adminjon");
        entity.setSurname("AdminAli");
        entity.setEmail("admin@gmail.com");
        entity.setPhone("998621524");
        entity.setPassword("123");
        entity.setRole(ProfileRole.ADMIN);
        entity.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(entity);

        log.info("Request for in it ADMIN {}", entity);
        return ResponseEntity.ok("Admin added");
    }
}
