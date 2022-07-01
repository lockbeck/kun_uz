package com.company.dto;

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
public class TypesDTO {
    private Integer id;
    @NotBlank(message = "key is null or empty MAZGI")
    private String key;
    @NotBlank(message = "nameUz is null or empty MAZGI")
    private String nameUz;
    @NotBlank(message = "nameEn is null or empty MAZGI")
    private String nameEn;
    @NotBlank(message = "nameRu is null or empty MAZGI")
    private String nameRu;
    private String name;
    private LocalDateTime createdDate;
    private Boolean visible;
}
