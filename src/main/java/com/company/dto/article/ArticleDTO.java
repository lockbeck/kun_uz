package com.company.dto.article;

import com.company.dto.AttachDTO;
import com.company.dto.CategoryDTO;
import com.company.dto.profile.ProfileDTO;
import com.company.dto.RegionDTO;
import com.company.enums.ArticleStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDTO {
    private String id;
    private String title;
    private String description;
    private String content;
    private Integer viewCount;
    private Integer sharedCount;
    private Integer LikeCount;

    private LocalDateTime publishedDate;
    private LocalDateTime createdDate;

    private ArticleStatus status;
    private Boolean visible;

    private ProfileDTO moderator;
    private ProfileDTO publisher;

    private RegionDTO region;
    private CategoryDTO category;
    private AttachDTO attach;


}
