package com.company.repository;

import com.company.dto.CategoryDTO;
import com.company.entity.CategoryEntity;
import com.company.entity.ProfileEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<CategoryEntity,Integer> {
    Optional<CategoryEntity> findByKey(String key);
}
