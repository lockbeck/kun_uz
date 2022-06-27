package com.company.dto.article;

import com.company.enums.ArticleStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleFilterDTO {
    private String id;
    private String title;
    private String description;

    private Integer regionId;
    private Integer categoryId;

    private Integer moderatorId;
    private Integer publisherId;

    private String publishedDateFrom;
    private String publishedDateTo;

    private ArticleStatus status;
}
