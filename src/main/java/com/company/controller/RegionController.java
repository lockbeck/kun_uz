package com.company.controller;

import com.company.dto.RegionDTO;
import com.company.enums.LangEnum;
import com.company.enums.ProfileRole;
import com.company.service.RegionService;
import com.company.util.HttpHeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/region")
public class RegionController {
    @Autowired
    private RegionService regionService;

    //public

    //secure


    @PostMapping("/adm")
    public ResponseEntity<RegionDTO> create(@RequestBody RegionDTO dto, HttpServletRequest request) {
        HttpHeaderUtil.getId(request,ProfileRole.ADMIN);
        RegionDTO regionDTO = regionService.create(dto);
        return ResponseEntity.ok(regionDTO);
    }

    @GetMapping("/adm/{id}")
    public ResponseEntity<RegionDTO>show(@PathVariable("id") Integer id,HttpServletRequest request){
        HttpHeaderUtil.getId(request,ProfileRole.ADMIN);
        RegionDTO dto = regionService.getRegionDTO(id);
        return ResponseEntity.ok(dto);

    }

    @PutMapping("/adm/{id}")
    public ResponseEntity<String>update(@RequestBody RegionDTO dto,@PathVariable("id") Integer id,HttpServletRequest request){
        HttpHeaderUtil.getId(request,ProfileRole.ADMIN);

        regionService.update(id,dto);
        return ResponseEntity.ok("Successful");
    }
    @DeleteMapping("/adm/{id}")
    public ResponseEntity<String>delete(@PathVariable("id") Integer id,HttpServletRequest request){
        HttpHeaderUtil.getId(request,ProfileRole.ADMIN);
        regionService.delete(id);
        return ResponseEntity.ok("Successful");

    }

    @GetMapping("/public")
//    public ResponseEntity<List<TypesDTO>> getAll(@PathVariable("lang" ,defaultValue = "uz") LangEnum langEnum) {
    public ResponseEntity<List<RegionDTO>> getAll(@RequestHeader(value = "Accept-language", defaultValue = "uz") LangEnum langEnum) {
        List<RegionDTO> typesDTOList = regionService.getList(langEnum);
        return ResponseEntity.ok(typesDTOList);
    }

    @GetMapping("/adm")
    public ResponseEntity<List<RegionDTO>> getList(HttpServletRequest request) {
        HttpHeaderUtil.getId(request,ProfileRole.ADMIN);
        List<RegionDTO> list = regionService.getListOnlyForAdmin();
        return ResponseEntity.ok().body(list);
    }




}
