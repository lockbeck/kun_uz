package com.company.dto.profile;

import com.company.entity.ArticleEntity;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
    private Integer id;
    private String name;
    private String phone;
    private String surname;
    private String email;
    private String password;
    private String photoId;

    private LocalDateTime createdDate;

    private Boolean visible;
    private ProfileStatus status;
    private ProfileRole role;

    private List<ArticleEntity> moderatorArticleList;
    private List<ArticleEntity> publisherArticleList;

    private String jwt;
}
