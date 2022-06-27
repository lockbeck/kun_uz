package com.company.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ArticleRequestDTO {
    private String articleId;
    private String attachId;
    private List<String> idList;
    private String regionKey;
    private String typeKey;

}
