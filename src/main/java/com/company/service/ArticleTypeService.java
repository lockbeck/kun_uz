package com.company.service;

import com.company.entity.ArticleEntity;
import com.company.entity.ArticleTypeEntity;
import com.company.entity.TypesEntity;
import com.company.repository.ArticleTypeRepository;
import com.company.repository.TypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleTypeService {
    @Autowired
    private ArticleTypeRepository articleTypeRepository;
    @Autowired
    private TypesService typesService;
     public void create(ArticleEntity article, List<Integer> typesList){
         for (Integer integer : typesList) {
             ArticleTypeEntity articleType = new ArticleTypeEntity();
             articleType.setArticle(article);
             articleType.setTypes(typesService.get(integer));
             articleTypeRepository.save(articleType);
         }
     }

    public void delete(String id) {
         articleTypeRepository.deleteByArticleId(id);
    }
}
