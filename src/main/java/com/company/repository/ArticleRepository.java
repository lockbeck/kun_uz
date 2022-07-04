package com.company.repository;

import com.company.entity.ArticleEntity;
import com.company.entity.CategoryEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ArticleStatus;
import com.company.mapper.ArticleShortInfoByCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends PagingAndSortingRepository<ArticleEntity, String> {


    @Modifying
    @Transactional
    @Query("update ArticleEntity a set a.status =:status where a.id=:articleId")
    void changeStatusNotPublish(@Param("articleId") String articleId, @Param("status") ArticleStatus status);

    @Modifying
    @Transactional
    @Query("update ArticleEntity a set a.status =:status, a.publishedDate =:time, a.publisher.id=:pid where a.id=:articleId")
    void changeStatusToPublish(@Param("articleId") String articleId, @Param("pid") Integer pId,
                               @Param("status") ArticleStatus status, @Param("time") LocalDateTime time);

    List<ArticleEntity> findTopByCategoryAndStatusAndVisibleTrueOrderByCreatedDateDesc(CategoryEntity entity, ArticleStatus status);

    @Query("SELECT new ArticleEntity(art.id,art.title,art.description,art.publishedDate) " +
            " from ArticleEntity  as art where art.category.key =:categoryKey and art.status =:status " +
            " and art.visible = true and art.status ='PUBLISHED'" +
            " order by art.publishedDate ")
    Page<ArticleEntity> findTop5ByArticleByCategory2(@Param("categoryKey") String categoryKey,
                                            @Param("status") ArticleStatus status, Pageable pageable);

    @Query(value = "select  art.id as id ,art.title as title, art.description as description,art.publish_date as publishedDate" +
            "   from article as art " +
            "   inner join category as cat on art.category_id = cat.id " +
            " where cat.key=:key and art.status='PUBLISHED' and art.visible=true  " +
            " order by art.publish_date limit 5 ",
            nativeQuery = true)
    List<ArticleShortInfoByCategory> findTop5ByArticleByCategory3(@Param("key") String key);


    @Query("SELECT new ArticleEntity(art.id,art.title,art.description,art.publishedDate) " +
            " from ArticleTypeEntity as a " +
            " inner join a.article as art " +
            " inner join a.types as t " +
            " Where t.key =:typeKey and art.visible = true and art.status = 'PUBLISHED' " +
            " order by art.publishedDate ")
    Page<ArticleEntity> findLast5ByType(@Param("typeKey") String typeKey, Pageable pageable);

    @Query("SELECT new ArticleEntity(art.id,art.title,art.description,art.publishedDate) " +
            " from ArticleTypeEntity as a " +
            " inner join a.article as art " +
            " inner join a.types as t " +
            " Where t.key =:typeKey and art.visible = true and art.status = 'PUBLISHED' " +
            " order by art.publishedDate ")
    Page<ArticleEntity> findLast3ByType(@Param("typeKey") String typeKey, Pageable pageable);

    @Query(value = "SELECT art.* " +
            " FROM article as art " +
            " inner join article_type as a on a.article_id = art.id " +
            " inner join types as t on t.id = a.types_id " +
            " where  t.key =:key  " +
            " order by art.publish_date " +
            " limit 5 ",
            nativeQuery = true)
    List<ArticleEntity> findTop5ByArticleNative(@Param("key") String key);

    @Query("SELECT new ArticleEntity(art.id,art.title,art.description,art.publishedDate) " +
            " from ArticleEntity as art " +
            " Where art.visible = true and art.status = 'PUBLISHED' and art.id not in (:idList) " +
            " order by art.publishedDate")
    Page<ArticleEntity> findLast8NotIn(@Param("idList") List<String> idList, @Param("type") Pageable pageable);

    //    Get Last 4 Article By Types and except given article id.
//    ArticleShortInfo
    @Query("SELECT new ArticleEntity(art.id,art.title,art.description,art.publishedDate) " +
            " from ArticleTypeEntity as a " +
            "inner join a.article as art " +
            "inner join a.types as t " +
            " Where t.key = :key and art.visible = true and art.status = 'PUBLISHED' and art.id not in (:idList) " +
            " order by art.publishedDate")
    Page<ArticleEntity> findLast4NotInListByType(@Param("idList") List<String> idList, @Param("key") String key, Pageable pageable);

    @Query("SELECT new ArticleEntity(a.id,a.title,a.description,a.publishedDate) " +
            " from ArticleEntity as a " +
            " Where a.visible = true and a.status = 'PUBLISHED' and a.id not in (:idList)" +
            " order by a.viewCount desc ,a.publishedDate desc")
    Page<ArticleEntity> mostRead4(@Param("idList") String id, Pageable pageable);

    @Modifying
    @Transactional
    @Query("update ArticleEntity set viewCount = viewCount+1 " +
            "where id = :id ")
    void increaseViewCount(@Param("id") String id);

    @Query("SELECT new ArticleEntity(art.id,art.title,art.description,art.publishedDate) " +
            " from ArticleTagEntity as a " +
            "inner join a.article as art " +
            "inner join a.tag as t " +
            " Where t.name = :tagName and art.visible = true and art.status = 'PUBLISHED' and art.id not in (:id) " +
            " order by art.publishedDate")
    Page<ArticleEntity> getLast4ByTagNameNotId(@Param("id") String id, @Param("tagName") String tagName, Pageable pageable);

    @Query("SELECT new ArticleEntity(art.id,art.title,art.description,art.publishedDate) " +
            " from ArticleTypeEntity as a " +
            "inner join a.types as tp " +
            "inner join a.article as art " +
            "inner join art.region as r " +
            " Where r.key = :regionKey and tp.key = :typeKey " +
            "and art.visible = true and art.status = 'PUBLISHED' " +
            " order by art.publishedDate")
    Page<ArticleEntity> getLast5ByTypeAndRegionNotId(@Param("typeKey") String typeKey, @Param("regionKey") String regionKey, Pageable pageable);

    @Modifying
    @Transactional
    @Query("update ArticleEntity as a set a.attach.id =?2 where a.id =?1")
    void updatePhotoById(String id, String imageId);

    @Query("SELECT new ArticleEntity(art.id,art.title,art.description,art.publishedDate) " +
            " from ArticleEntity as art " +
            "inner join art.region as r " +
            " Where art.region.key = :typeKey " +
            "and art.visible = true and art.status = 'PUBLISHED' ")
    Page<ArticleEntity> getListPaginationByRegionKey(@Param("typeKey") String regionKey, Pageable pageable);
//    @Modifying
//    @Transactional
//    @Query("update ArticleEntity a set a.status =:status where a.id=:articleId")
//    void changeStatusNotPublish(@Param("articleId") String articleId, @Param("pid") Integer pId);
}
