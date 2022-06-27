package com.company.dto.profile;

import com.company.entity.ArticleEntity;
import com.company.enums.ArticleStatus;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileFilterDTO {
    private Integer id;
    private String name;
    private String phone;
    private String surname;
    private String email;

    private String photoId;

    private String createdDateFrom;
    private String createdDateTo;

    private Boolean visible;
    private ProfileStatus status;
    private ProfileRole role;

}
