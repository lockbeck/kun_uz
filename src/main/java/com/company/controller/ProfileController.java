package com.company.controller;

import com.company.dto.profile.ProfileDTO;
import com.company.dto.article.ArticleDTO;
import com.company.dto.article.ArticleFilterDTO;
import com.company.dto.profile.ProfileFilterDTO;
import com.company.enums.ProfileRole;
import com.company.service.ProfileService;
import com.company.util.HttpHeaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/profile")
@Slf4j
public class ProfileController {
    @Autowired
    private ProfileService profileService;


// 1. Create profile (ADMIN, (can create moderator,publisher))
    @PostMapping("/adm/create")
    public ResponseEntity<ProfileDTO> create(@RequestBody ProfileDTO dto,
                                             HttpServletRequest request) {
        log.info("Request to create profile {}", dto);
        HttpHeaderUtil.getId(request , ProfileRole.ADMIN);
        ProfileDTO profileDTO = profileService.create(dto);
        return ResponseEntity.ok(profileDTO);
    }
    @PutMapping("/adm/update/{id}")
    public ResponseEntity<String>update(@RequestBody ProfileDTO dto,
                                        @PathVariable("id") Integer id,
                                        HttpServletRequest request){
        log.info("Request to update profile {}", dto);
        HttpHeaderUtil.getId(request , ProfileRole.ADMIN);
        profileService.update(id,dto);
        return ResponseEntity.ok("Successful");
    }
    @GetMapping("/adm/list")
    public ResponseEntity<?> getAll(HttpServletRequest request){
        log.info("Request to get profile list");
        HttpHeaderUtil.getId(request , ProfileRole.ADMIN);
        List<ProfileDTO> all = profileService.getAll();
        return ResponseEntity.ok().body(all);
    }
    @DeleteMapping("/adm/{id}")
    public ResponseEntity<String>delete(@PathVariable("id") Integer id,
                                        HttpServletRequest request){
        log.info("Request to delete profile by id {}", id);
        HttpHeaderUtil.getId(request , ProfileRole.ADMIN);
        profileService.delete(id);
        return ResponseEntity.ok("Successful");

    }




    @PutMapping("/update")
    public ResponseEntity<String>update(@RequestBody ProfileDTO dto,
                                        HttpServletRequest request){
        Integer id = HttpHeaderUtil.getId(request);
        profileService.update(id,dto);
        return ResponseEntity.ok("Successful");
    }
    @PutMapping("/setPhoto/{photoId}")
    public ResponseEntity<String> setPhoto(@PathVariable("photoId") String photoId,
                                             HttpServletRequest request) {
        Integer userId = HttpHeaderUtil.getId(request);
        String response = profileService.setPhoto(userId, photoId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/adm/filter")
    public ResponseEntity<List<ProfileDTO>> filter(@RequestParam(value = "page", defaultValue = "1")int page,
                                                    @RequestParam(value = "size", defaultValue = "4")int size,
                                                    @RequestBody ProfileFilterDTO dto,
                                                   HttpServletRequest request) {
        HttpHeaderUtil.getId(request,ProfileRole.ADMIN);
        List<ProfileDTO> response = profileService.filter(dto,page,size);
        return ResponseEntity.ok().body(response);
    }



}
