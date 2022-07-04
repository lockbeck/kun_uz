package com.company.controller;

import com.company.dto.TypesDTO;
import com.company.dto.TypesPaginationDTO;
import com.company.enums.LangEnum;
import com.company.enums.ProfileRole;
import com.company.service.TypesService;
import com.company.util.HttpHeaderUtil;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/types")
public class TypesController {
    @Autowired
    private TypesService typesService;


// 1. Create  (ADMIN)
//        (key,name_uz, name_ru, name_en)
    @PostMapping("/adm/create")
    public ResponseEntity<TypesDTO> create(@RequestBody TypesDTO dto, HttpServletRequest request) {
        HttpHeaderUtil.getId(request ,ProfileRole.ADMIN);
        TypesDTO typesDTO = typesService.create(dto);
        return ResponseEntity.ok(typesDTO);
    }
//     2. Update by id (ADMIN)
//    (key,name_uz, name_ru, name_en)
    @PutMapping("/adm/{id}")
    public ResponseEntity<String>update(@RequestBody TypesDTO dto,@PathVariable("id") Integer id,HttpServletRequest request){
        HttpHeaderUtil.getId(request ,ProfileRole.ADMIN);
        typesService.update(id,dto);
        return ResponseEntity.ok("Successful");
    }

//       3. Delete by id (ADMIN)
    @DeleteMapping("/adm/{id}")
//    public ResponseEntity<String>delete(@PathVariable("id") Integer id,@RequestHeader("Authorization")String jwt){
    public ResponseEntity<String>delete(@PathVariable("id") Integer id,HttpServletRequest request){
        HttpHeaderUtil.getId(request ,ProfileRole.ADMIN);
        typesService.delete(id);
        return ResponseEntity.ok("Successful");

    }
//    4. Get List (ADMIN) (Pagination)
//    (id,key,name_uz, name_ru, name_en,visible,created_date)
    @GetMapping("/pagination")
    public ResponseEntity<PageImpl> getPagination(@RequestHeader(value = "Accept-language", defaultValue = "uz") LangEnum langEnum,
                                                  @RequestParam(value = "page", defaultValue = "1") int page,
                                                  @RequestParam(value = "size", defaultValue = "5") int size,
                                                  HttpServletRequest request) {
        HttpHeaderUtil.getId(request ,ProfileRole.ADMIN);
        PageImpl response = typesService.pagination(page, size,langEnum);
        return ResponseEntity.ok().body(response);
    }



//    5. Get By Lang (Language keladi shu language dagi name larini berib yuboramiz)
//    (id,key,name) (name ga tegishli name_.. dagi qiymat qo'yiladi.)
    @GetMapping("/public")
//    public ResponseEntity<List<TypesDTO>> getAll(@PathVariable("lang" ,defaultValue = "uz") LangEnum langEnum) {
    public ResponseEntity<List<TypesDTO>> getAll(@RequestHeader(value = "Accept-language", defaultValue = "uz") LangEnum langEnum) {
        List<TypesDTO> typesDTOList = typesService.getList(langEnum);
        return ResponseEntity.ok(typesDTOList);
    }




}
