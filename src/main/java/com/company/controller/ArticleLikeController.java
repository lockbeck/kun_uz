package com.company.controller;

import com.company.dto.article.ArticleCreateDTO;
import com.company.dto.article.ArticleDTO;
import com.company.dto.article.ArticleLikeDTO;
import com.company.enums.ProfileRole;
import com.company.service.ArticleLikeService;
import com.company.util.HttpHeaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
@Slf4j
@RequestMapping("/article_like")
@RestController
public class ArticleLikeController {

    @Autowired
    private ArticleLikeService articleLikeService;

//     16. Article LikeCreate (ANY)
//        (article_id)
    @PostMapping("/like")
    public ResponseEntity<Void> like(@RequestBody ArticleLikeDTO dto,
                                     HttpServletRequest request) {
        log.info("Request to like {}",dto);
        Integer profileId = HttpHeaderUtil.getId(request);
        articleLikeService.articleLike(dto.getArticleId(), profileId);
        return ResponseEntity.ok().build();
    }

//    17. Article DisLikeCreate (ANY)
//            (article_id)
    @PostMapping("/dislike")
    public ResponseEntity<Void> dislike(@RequestBody ArticleLikeDTO dto,
                                        HttpServletRequest request) {
        log.info("Request to dislike {}",dto);
        Integer profileId = HttpHeaderUtil.getId(request);
        articleLikeService.articleDisLike(dto.getArticleId(), profileId);
        return ResponseEntity.ok().build();
    }

//    18. Article Like Remove (ANY)
//        (article_id)
    @PostMapping("/remove")
    public ResponseEntity<Void> remove(@RequestBody ArticleLikeDTO dto,
                                       HttpServletRequest request) {
        log.info("Request to remove {}",dto);
        Integer profileId = HttpHeaderUtil.getId(request);
        articleLikeService.removeLike(dto.getArticleId(), profileId);
        return ResponseEntity.ok().build();
    }

}
