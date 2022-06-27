package com.company.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
@Getter
@Setter
@ToString
 @JsonInclude(JsonInclude.Include.NON_NULL)

public class RegionDTO {
    private Integer id;
    private String key;
    private String nameUz;
    private String nameEn;
    private String nameRu;
    private String name;
    private LocalDateTime createdDate;
    private Boolean visible;

    public RegionDTO(Integer id, String key) {
        this.id = id;
        this.key = key;
    }

    public RegionDTO() {
    }
}
