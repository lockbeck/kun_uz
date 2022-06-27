package com.company.repository;

import com.company.entity.ArticleEntity;
import com.company.entity.TagEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends CrudRepository<TagEntity, Integer> {
    Optional<TagEntity> findByName(String name);
    boolean existsByName(String name);

    @Query(value = "SELECT t.name " +
            " FROM tag as t  " +
            " inner join article_tag as ata on ata.tag_id =t.id " +
            " where  ata.article_id =:articleId  " ,
            nativeQuery = true)
    List<String> getTagNameByArticleId(@Param("articleId") String articleId);


}
