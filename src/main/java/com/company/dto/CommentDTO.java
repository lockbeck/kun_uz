package com.company.dto;

import com.company.dto.article.ArticleDTO;
import com.company.dto.profile.ProfileDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {
    private Integer id;
    @NotBlank(message = "content is null or empty MAZGI")
    private String content;
    @NotBlank(message = "articleId is null or empty MAZGI")
    private String articleId;
    private Integer replyId;


    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean visible;

    private ProfileDTO profile;
    private ArticleDTO article;
}
