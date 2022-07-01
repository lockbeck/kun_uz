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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDTO {
    private String id;
    @NotNull(message = "Title required MAZGI")
    @Size(min = 10, max = 255,message = "title must be between 10 and 255 characters")
    private String title;
    @NotBlank(message = "Description required MAZGI")
    private String description;
    @NotBlank(message = "Content required MAZGI")
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
