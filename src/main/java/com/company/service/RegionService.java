package com.company.service;

import com.company.dto.RegionDTO;
import com.company.entity.RegionEntity;
import com.company.enums.LangEnum;
import com.company.exp.BadRequestException;
import com.company.exp.ItemNotFoundException;
import com.company.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class RegionService {
    @Autowired
    private RegionRepository  regionRepository;
    public RegionDTO create(RegionDTO dto) {
        isValid(dto);
        Optional<RegionEntity> byKey = regionRepository.findByKey(dto.getKey());

        if (byKey.isPresent()) {
            throw new BadRequestException("Region already exists");
        }

        RegionEntity entity = new RegionEntity();
        entity.setKey(dto.getKey());
        entity.setNameUz(dto.getNameUz());
        entity.setNameEn(dto.getNameEn());
        entity.setNameRu(dto.getNameRu());
        regionRepository.save(entity);

        dto.setId(entity.getId());
        return dto;
    }

    public RegionDTO getRegionDTO(Integer id) {
        RegionEntity entity = get(id);
        if (entity.getVisible().equals(Boolean.FALSE)) {
            throw new BadRequestException("Region is deleted");
        }
        RegionDTO dto = new RegionDTO();
        dto.setKey(entity.getKey());
        dto.setNameUz(entity.getNameUz());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        return dto;

    }

    public void update(Integer id, RegionDTO dto) {
        isValid(dto);
        RegionEntity entity = get(id);
        entity.setKey(dto.getKey());
        entity.setNameUz(dto.getNameUz());
        entity.setNameEn(dto.getNameEn());
        entity.setNameRu(dto.getNameRu());
        regionRepository.save(entity);

    }

    public void delete(Integer id) {
        RegionEntity entity = get(id);
        entity.setVisible(Boolean.FALSE);
        regionRepository.save(entity);
    }

    public RegionEntity get(Integer id) {
        return regionRepository.findById(id).orElseThrow(() -> {
                    throw new ItemNotFoundException("Region Not found");
                }
        );
    }

    private void isValid(RegionDTO dto) {
        if (dto.getKey() == null){
            throw new BadRequestException("key is wrong");
        }
        if (dto.getNameUz() == null){
            throw new BadRequestException("name uz is wrong");
        }
        if (dto.getNameEn() == null){
            throw new BadRequestException("name en is wrong");
        }
        if (dto.getNameRu() == null){
            throw new BadRequestException("name ru is wrong");
        }
    }
    public List<RegionDTO> getListOnlyForAdmin() {

        Iterable<RegionEntity> all = regionRepository.findAll();
        List<RegionDTO> dtoList = new LinkedList<>();

        all.forEach(typesEntity -> {
            RegionDTO dto = new RegionDTO();
            dto.setKey(typesEntity.getKey());
            dto.setNameUz(typesEntity.getNameUz());
            dto.setNameRu(typesEntity.getNameRu());
            dto.setNameEn(typesEntity.getNameEn());
            dtoList.add(dto);
        });
        return dtoList;
    }

    public List<RegionDTO> getList(LangEnum lang){
        Iterable<RegionEntity> all = regionRepository.findAll();
        List<RegionDTO> dtoList = new LinkedList<>();
        all.forEach(typesEntity -> {
            RegionDTO dto = new RegionDTO();
            dto.setKey(typesEntity.getKey());
            switch (lang) {
                case ru:
                    dto.setName(typesEntity.getNameRu());
                    break;
                case en:
                    dto.setName(typesEntity.getNameEn());
                    break;
                case uz:
                    dto.setName(typesEntity.getNameUz());
                    break;
            }
            dtoList.add(dto);
        });
        return dtoList;
    }
    public RegionDTO getArticleKeyAndName(RegionEntity entity, LangEnum langEnum){

        RegionDTO regionDTO = new RegionDTO();

        regionDTO.setKey(entity.getKey());
        switch (langEnum){
            case uz:
                regionDTO.setName(entity.getNameUz());
                break;
            case ru:
                regionDTO.setName(entity.getNameRu());
                break;
            case en:
                regionDTO.setName(entity.getNameEn());
                break;
            default:
                regionDTO.setName(entity.getNameUz());
        }
        return regionDTO;
    }
}
