package com.company.controller;

import com.company.dto.CommentDTO;
import com.company.dto.RegionDTO;
import com.company.enums.ProfileRole;
import com.company.service.CommnetService;
import com.company.service.RegionService;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommnetService commnetService;

    //public

    //secure


    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody CommentDTO dto, @RequestHeader("Authorization") String jwt) {
        Integer integer = JwtUtil.decode(jwt);
        commnetService.create(dto,integer);
        return ResponseEntity.ok(dto.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<String>show(@PathVariable("id") Integer id){
        String  dto = commnetService.get(id);
        return ResponseEntity.ok(dto);

    }
    @GetMapping("/all")
    public ResponseEntity<List<CommentDTO>>show(@RequestHeader("Authorization") String jwt){
        JwtUtil.decode(jwt,ProfileRole.ADMIN);
        List<CommentDTO>  dto = commnetService.getAll();
        return ResponseEntity.ok(dto);

    }

    @PutMapping("/{id}")
    public ResponseEntity<String>update(@RequestBody CommentDTO dto,@PathVariable("id") Integer id,@RequestHeader("Authorization") String jwt){
        Integer decode = JwtUtil.decode(jwt);
        commnetService.update(id,dto.getContent(),decode);
        return ResponseEntity.ok("Successful");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String>delete(@PathVariable("id") Integer id,@RequestHeader("Authorization") String jwt){
        Integer decode = JwtUtil.decode(jwt);
        commnetService.delete(id,decode);
        return ResponseEntity.ok("Successful");

    }




}
