package com.company.service;

import com.company.dto.CategoryDTO;
import com.company.dto.RegionDTO;
import com.company.dto.TypesDTO;
import com.company.dto.article.ArticleDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.CategoryEntity;
import com.company.entity.RegionEntity;
import com.company.entity.TypesEntity;
import com.company.enums.LangEnum;
import com.company.exp.BadRequestException;
import com.company.exp.ItemNotFoundException;
import com.company.repository.CategoryRepository;
import com.company.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    public CategoryDTO create(CategoryDTO dto) {
        isValid(dto);
        Optional<CategoryEntity> byKey = categoryRepository.findByKey(dto.getKey());
        if (byKey.isPresent()) {
            throw new BadRequestException("Category already exists");
        }
        CategoryEntity entity = new CategoryEntity();
        entity.setKey(dto.getKey());
        entity.setNameUz(dto.getNameUz());
        entity.setNameEn(dto.getNameEn());
        entity.setNameRu(dto.getNameRu());
        categoryRepository.save(entity);

        dto.setId(entity.getId());
        return dto;
    }

    public CategoryDTO show(Integer id) {
        CategoryEntity entity = get(id);
        CategoryDTO dto = new CategoryDTO();
        dto.setKey(entity.getKey());
        dto.setNameUz(entity.getNameUz());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        return dto;

    }

    public void update(Integer id, CategoryDTO dto) {
        isValid(dto);
        CategoryEntity entity = get(id);
        entity.setKey(dto.getKey());
        entity.setNameUz(dto.getNameUz());
        entity.setNameEn(dto.getNameEn());
        entity.setNameRu(dto.getNameRu());
        categoryRepository.save(entity);
    }

    public void delete(Integer id) {
        CategoryEntity entity = get(id);
        entity.setVisible(Boolean.FALSE);
        categoryRepository.save(entity);
    }

    public CategoryEntity get(Integer id) {
        return categoryRepository.findById(id).orElseThrow(() -> {
                    throw new ItemNotFoundException("Category Not found");
                }
        );
    }
    public CategoryEntity get(String key) {
        return categoryRepository.findByKey(key).orElseThrow(() -> {
                    throw new ItemNotFoundException("Category Not found");
                }
        );
    }

    private void isValid(CategoryDTO dto) {
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

    public List<CategoryDTO> getList(LangEnum lang){
        Iterable<CategoryEntity> all = categoryRepository.findAll();
        List<CategoryDTO> dtoList = new LinkedList<>();
        all.forEach(typesEntity -> {
            CategoryDTO dto = new CategoryDTO();
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


    public List<CategoryDTO> getListOnlyForAdmin() {

        Iterable<CategoryEntity> all = categoryRepository.findAll();
        List<CategoryDTO> dtoList = new LinkedList<>();

        all.forEach(typesEntity -> {
            CategoryDTO dto = new CategoryDTO();
            dto.setKey(typesEntity.getKey());
            dto.setNameUz(typesEntity.getNameUz());
            dto.setNameRu(typesEntity.getNameRu());
            dto.setNameEn(typesEntity.getNameEn());
            dtoList.add(dto);
        });
        return dtoList;
    }
    public CategoryDTO getArticleKeyAndName(CategoryEntity entity,LangEnum langEnum){

        CategoryDTO categoryDTO = new CategoryDTO();

        categoryDTO.setKey(entity.getKey());
        switch (langEnum){
            case uz:
                categoryDTO.setName(entity.getNameUz());
                break;
            case ru:
                categoryDTO.setName(entity.getNameRu());
                break;
            case en:
                categoryDTO.setName(entity.getNameEn());
                break;
            default:
                categoryDTO.setName(entity.getNameUz());
        }
          return categoryDTO;
    }
}
