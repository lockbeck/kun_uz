package com.company.controller;

import com.company.dto.CategoryDTO;
import com.company.dto.JwtDTO;
import com.company.dto.RegionDTO;
import com.company.dto.TypesDTO;
import com.company.enums.LangEnum;
import com.company.enums.ProfileRole;
import com.company.service.CategoryService;
import com.company.service.RegionService;
import com.company.util.HttpHeaderUtil;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;



    /*@PostMapping("/create")
    public ResponseEntity<CategoryDTO> create(@RequestBody CategoryDTO dto,
                                              @RequestHeader("Authorization") String jwt) {
        JwtUtil.decode(jwt, ProfileRole.ADMIN);
        CategoryDTO categoryDTO = categoryService.create(dto);
        return ResponseEntity.ok(categoryDTO);
    }*/
    @PostMapping("/create")
    public ResponseEntity<CategoryDTO> create(@RequestBody CategoryDTO dto,
                                              HttpServletRequest request) {

        HttpHeaderUtil.getId(request ,ProfileRole.ADMIN);
        CategoryDTO categoryDTO = categoryService.create(dto);
        return ResponseEntity.ok(categoryDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO>show(@PathVariable("id") Integer id,@RequestHeader("Authorization")String jwt){
        JwtUtil.decode(jwt,ProfileRole.ADMIN);
        CategoryDTO dto = categoryService.show(id);
        return ResponseEntity.ok(dto);

    }

    @PutMapping("/{id}")
    public ResponseEntity<String>update(@RequestBody CategoryDTO dto,@PathVariable("id") Integer id,@RequestHeader("Authorization")String jwt){
        JwtUtil.decode(jwt,ProfileRole.ADMIN);
        categoryService.update(id,dto);
        return ResponseEntity.ok("Successful");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String>delete(@PathVariable("id") Integer id,@RequestHeader("Authorization")String jwt){
        JwtUtil.decode(jwt,ProfileRole.ADMIN);
        categoryService.delete(id);
        return ResponseEntity.ok("Successful");

    }
    @GetMapping("/public")
//    public ResponseEntity<List<CategoryDTO>> getAll(@PathVariable("lang" ,defaultValue = "uz") LangEnum langEnum) {
    public ResponseEntity<List<CategoryDTO>> getAll(@RequestHeader(value = "Accept-language", defaultValue = "uz") LangEnum langEnum) {
        List<CategoryDTO> categoryDTOS = categoryService.getList(langEnum);
        return ResponseEntity.ok(categoryDTOS);
    }

    @GetMapping("")
    public ResponseEntity<List<CategoryDTO>> getList(@RequestHeader("Authorization") String jwt) {
        JwtUtil.decode(jwt, ProfileRole.ADMIN);
        List<CategoryDTO> list = categoryService.getListOnlyForAdmin();
        return ResponseEntity.ok().body(list);
    }




}
