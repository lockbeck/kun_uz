package com.company.service;

import com.company.entity.*;
import com.company.repository.ArticleTagRepository;
import com.company.repository.ArticleTypeRepository;
import com.company.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ArticleTagService {
    @Autowired
    private ArticleTagRepository articleTagRepository;
    @Autowired
    private TagService tagService;
    public void create(ArticleEntity article, List<String> tagList){
        for (String name : tagList) {
            TagEntity tag = tagService.createIfNotExists(name);
            ArticleTagEntity articleTag = new ArticleTagEntity();
            articleTag.setArticle(article);
            articleTag.setTag(tag);
            articleTagRepository.save(articleTag);
        }
    }





}
