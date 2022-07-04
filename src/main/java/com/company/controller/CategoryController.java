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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

//        1. Create  (ADMIN)
//        (key,name_uz, name_ru, name_en)
    @PostMapping("/create")
    public ResponseEntity<CategoryDTO> create(@RequestBody CategoryDTO dto,
                                              HttpServletRequest request) {
        log.info("Request for create category {}",dto);
        HttpHeaderUtil.getId(request ,ProfileRole.ADMIN);
        CategoryDTO categoryDTO = categoryService.create(dto);
        return ResponseEntity.ok(categoryDTO);
    }

//        2. Update by id (ADMIN)
//        (key,name_uz, name_ru, name_en)
    @PutMapping("/{id}")
    public ResponseEntity<String>update(@RequestBody CategoryDTO dto,@PathVariable("id") Integer id,HttpServletRequest request){
        log.info("Request for update category by id {}",id);
        HttpHeaderUtil.getId(request ,ProfileRole.ADMIN);
        categoryService.update(id,dto);
        return ResponseEntity.ok("Successful");
    }

//    3. Delete by id (ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<String>delete(@PathVariable("id") Integer id,HttpServletRequest request){
        log.info("Request for delete category by id {}",id);
        HttpHeaderUtil.getId(request ,ProfileRole.ADMIN);
        categoryService.delete(id);
        return ResponseEntity.ok("Successful");

    }

//      4. Get List (ADMIN)
//        (id,key,name_uz, name_ru, name_en,visible,created_date)
    @GetMapping("")
    public ResponseEntity<List<CategoryDTO>> getList(HttpServletRequest request) {
        log.info("Request for get all category for ADMIN");
        HttpHeaderUtil.getId(request ,ProfileRole.ADMIN);
        List<CategoryDTO> list = categoryService.getListOnlyForAdmin();
        return ResponseEntity.ok().body(list);
    }

//    5. Get By Lang (Language keladi shu language dagi name larini berib yuboramiz)
//        (id,key,name) (name ga tegishli name_.. dagi qiymat qo'yiladi.)
    @GetMapping("/public")
//    public ResponseEntity<List<CategoryDTO>> getAll(@PathVariable("lang" ,defaultValue = "uz") LangEnum langEnum) {
    public ResponseEntity<List<CategoryDTO>> getAll(@RequestHeader(value = "Accept-language", defaultValue = "uz") LangEnum langEnum) {
        log.info("Request for get all category for public by language{}",langEnum);
        List<CategoryDTO> categoryDTOS = categoryService.getList(langEnum);
        return ResponseEntity.ok(categoryDTOS);
    }




}
