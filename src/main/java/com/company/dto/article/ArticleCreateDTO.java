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
    @NotNull(message = "Title required MAZGI")
    @Size(min = 10, max = 255,message = "title must be between 10 and 255 characters")
    private String title;
//    @NotNull(message = "description qani mazgi")
    @NotBlank(message = "Description required MAZGI")
    private String description;
    @NotNull(message = "Content required MAZGI")
    private String content;

    private String imageId;
    @PositiveOrZero(message = "positive number must be entered as region id MAZGI")
    private Integer regionId;
    @PositiveOrZero(message = "positive number must be entered as category id MAZGI")
    private Integer categoryId;

    @NotNull(message = "typeList required MAZGI")
    private List<Integer> typesList;
    @NotNull(message = "tagList required MAZGI")
    private List<String> tagList;

}
