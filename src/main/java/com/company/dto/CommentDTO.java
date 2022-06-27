package com.company.dto;

import com.company.dto.article.ArticleDTO;
import com.company.dto.profile.ProfileDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {
    private Integer id;
    private String content;
    private Integer replyId;


    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean visible;

    private ProfileDTO profile;
    private ArticleDTO article;
}
