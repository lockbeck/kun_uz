package com.company.service;

import com.company.dto.TypesDTO;
import com.company.entity.TypesEntity;
import com.company.enums.LangEnum;
import com.company.exp.BadRequestException;
import com.company.exp.ItemNotFoundException;
import com.company.repository.TypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class TypesService {
    @Autowired
    private TypesRepository typesRepository;
    public TypesDTO create(TypesDTO dto) {
        isValid(dto);
        Optional<TypesEntity> byKey = typesRepository.findByKey(dto.getKey());
        if (byKey.isPresent()) {
            throw new BadRequestException("Region already exists");
        }
        TypesEntity entity = new TypesEntity();
        entity.setKey(dto.getKey());
        entity.setNameUz(dto.getNameUz());
        entity.setNameEn(dto.getNameEn());
        entity.setNameRu(dto.getNameRu());
        typesRepository.save(entity);

        dto.setId(entity.getId());
        return dto;
    }
    public List<TypesDTO> getList(LangEnum lang){
        Iterable<TypesEntity> all = typesRepository.findAll();
        List<TypesDTO> dtoList = new LinkedList<>();
        all.forEach(typesEntity -> {
            TypesDTO dto = new TypesDTO();
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

    public TypesDTO show(Integer id) {
        TypesEntity entity = get(id);

        TypesDTO dto = new TypesDTO();
        dto.setKey(entity.getKey());
        dto.setNameUz(entity.getNameUz());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        return dto;

    }

    public void update(Integer id, TypesDTO dto) {
        isValid(dto);
        TypesEntity entity = get(id);

        entity.setKey(dto.getKey());
        entity.setNameUz(dto.getNameUz());
        entity.setNameEn(dto.getNameEn());
        entity.setNameRu(dto.getNameRu());
        typesRepository.save(entity);

    }

    public void delete(Integer id) {
        TypesEntity entity = get(id);
        entity.setVisible(Boolean.FALSE);
        typesRepository.save(entity);
    }

    public TypesEntity get(Integer id) {
        return typesRepository.findById(id).orElseThrow(() -> {
                    throw new ItemNotFoundException("Category Not found");
                }
        );
    }

    private void isValid(TypesDTO dto) {
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

    public List<TypesDTO> sortByName(){
        Sort sort = Sort.by(Sort.Direction.DESC, "id ");
        Iterable<TypesEntity> all = typesRepository.findAll(sort);


        return null;
    }
    public PageImpl pagination(int page, int size,LangEnum langEnum) {
        // page = 1
    /*    Iterable<TypesEntity> all = typesRepository.pagination(size, size * (page - 1));
        long totalAmount = typesRepository.countAllBy();*/
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<TypesEntity> all = typesRepository.findAll(pageable);

//        long totalAmount = all.getTotalElements();
//        int totalPages = all.getTotalPages();
        List<TypesEntity> list = all.getContent();

        List<TypesDTO> dtoList = new LinkedList<>();

        list.forEach(typesEntity -> {
            TypesDTO dto = new TypesDTO();
            dto.setId(typesEntity.getId());
            dto.setKey(typesEntity.getKey());
            dto.setNameUz(typesEntity.getNameUz());
            dto.setNameRu(typesEntity.getNameRu());
            dto.setNameEn(typesEntity.getNameEn());
            dtoList.add(dto);
        });

//        TypesPaginationDTO paginationDTO = new TypesPaginationDTO(totalAmount, dtoList);
//        return paginationDTO;
        return new PageImpl(dtoList,pageable, all.getTotalElements());
    }

    public List<TypesDTO> getListOnlyForAdmin() {

        Iterable<TypesEntity> all = typesRepository.findAll();
        List<TypesDTO> dtoList = new LinkedList<>();

        all.forEach(typesEntity -> {
            TypesDTO dto = new TypesDTO();
            dto.setKey(typesEntity.getKey());
            dto.setNameUz(typesEntity.getNameUz());
            dto.setNameRu(typesEntity.getNameRu());
            dto.setNameEn(typesEntity.getNameEn());
            dtoList.add(dto);
        });
        return dtoList;
    }
}
