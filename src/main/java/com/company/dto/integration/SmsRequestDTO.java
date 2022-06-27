package com.company.dto.integration;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SmsRequestDTO {
    private String key;
    private String phone;
    private String message;
}
