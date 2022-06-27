package com.company.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseInfoDTO {
    private Integer status;
    private String message;

    public ResponseInfoDTO(Integer status) {
        this.status = status;
    }

    public ResponseInfoDTO(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
