package com.company.repository;

import com.company.entity.ArticleTypeEntity;
import com.company.entity.ProfileEntity;
import com.company.entity.TypesEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ArticleTypeRepository extends CrudRepository<ArticleTypeEntity,Integer> {

    void deleteByArticleId(String id);
}
