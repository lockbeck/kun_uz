package com.company.dto.article;

import com.company.dto.CategoryDTO;
import com.company.dto.RegionDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleFullInfoDTO {
    private String id;
    private String title;
    private String description;
    private String content;
    private Integer viewCount;
    private Integer sharedCount;
    private Integer LikeCount;

    private LocalDateTime publishedDate;

    private List<String> tagList;
    private RegionDTO region;
    private CategoryDTO category;


}
