package com.company.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleCreateDTO {
    @NotNull(message = "title required")
    @Size(min = 10, max = 255,message = "title must be between 10 and 255 characters")
    private String title;
//    @NotNull(message = "description qani mazgi")
    @NotBlank(message = "description qani mazgi")
    private String description;
    @NotNull(message = "Content qani mazgi")
    private String content;

    private String imageId;
    @PositiveOrZero(message = "positive must be entered MAZGI")
    private Integer regionId;
    private Integer categoryId;

    private List<Integer> typesList;
    private List<String> tagList;

}
