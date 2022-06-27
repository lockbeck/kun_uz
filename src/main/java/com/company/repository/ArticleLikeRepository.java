package com.company.repository;

import com.company.entity.ArticleEntity;
import com.company.entity.ArticleLikeEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.LikeStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.spec.OAEPParameterSpec;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface ArticleLikeRepository extends CrudRepository<ArticleLikeEntity, Integer> {

    Optional<ArticleLikeEntity> findByArticleAndProfile(ArticleEntity article, ProfileEntity profile);

    @Query("FROM ArticleLikeEntity a where  a.article.id=:articleId and a.profile.id =:profileId")
    Optional<ArticleLikeEntity> findExists(String articleId, Integer profileId);

    @Transactional
    @Modifying
    @Query("DELETE FROM ArticleLikeEntity a where  a.article.id=:articleId and a.profile.id =:profileId")
    void delete(String articleId, Integer profileId);

//    Integer countByStatusAndArticleId(String articleId , LikeStatus status);

    @Query(value = "SELECT cast(SUM (CASE WHEN t.status = 'LIKE'  THEN 1 ELSE 0 END)as int) AS likes," +
            "cast(SUM (CASE WHEN t.status = 'DISLIKE'   THEN 1 ELSE 0 END)as int) AS dislikes " +
            "FROM article_like t where article_id = ?1 " ,
            nativeQuery = true)
    HashMap<String , Integer> countLikes(String id);


}
