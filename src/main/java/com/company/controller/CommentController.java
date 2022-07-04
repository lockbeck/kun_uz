package com.company.controller;

import com.company.dto.CommentDTO;
import com.company.dto.RegionDTO;
import com.company.enums.ProfileRole;
import com.company.service.CommnetService;
import com.company.service.RegionService;
import com.company.util.HttpHeaderUtil;
import com.company.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController {
    @Autowired
    private CommnetService commnetService;

    //public

    //secure

//1. CREATE (ANY)
//        (content,article_id,reply_id)
    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody @Valid CommentDTO dto,
                                         HttpServletRequest request) {
        log.info("Request for create comment {}",dto);
        Integer integer = HttpHeaderUtil.getId(request);
        commnetService.create(dto,integer);
        return ResponseEntity.ok(dto.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<String>show(@PathVariable("id") Integer id){
        log.info("Request for get comment by id {}",id);
        String  dto = commnetService.get(id);
        return ResponseEntity.ok(dto);

    }
    @GetMapping("/all")
    public ResponseEntity<List<CommentDTO>>show(HttpServletRequest request){
        log.info("Request for get all comment");
        HttpHeaderUtil.getId(request,ProfileRole.ADMIN);
        List<CommentDTO>  dto = commnetService.getAll();
        return ResponseEntity.ok(dto);

    }

    @PutMapping("/{id}")
    public ResponseEntity<String>update(@RequestBody CommentDTO dto,
                                        @PathVariable("id") Integer id,
                                        HttpServletRequest request){
        log.info("Request for update comment {} by id {}",dto, id);
        Integer decode = HttpHeaderUtil.getId(request);
        commnetService.update(id,dto.getContent(),decode);
        return ResponseEntity.ok("Successful");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String>delete(@PathVariable("id") Integer id,
                                        HttpServletRequest request){
        log.info("Request for delete comment by id {}",id);
        Integer decode = HttpHeaderUtil.getId(request);
        commnetService.delete(id,decode);
        return ResponseEntity.ok("Successful");

    }




}
