package com.company.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistrationDTO {
    @NotBlank(message = "name is null or empty MAZGI")
    private String name;
    @NotBlank(message = "surname is null or empty MAZGI")
    private String surname;
    @NotBlank(message = "email is null or empty MAZGI")
    @Pattern(regexp = "^(.+)@(.+)$", message = "email format error MAZGI")
    private String email;
    @NotBlank(message = "phone is null or empty MAZGI")
    @Pattern(regexp = "9989[0-9]{8}", message = "phone format error MAZGI")
    private String phone;
    @NotBlank(message = "password is null or empty MAZGI")
    @Size(min = 3,max = 100, message = "password length must be >=3 and <=100")
    private String password;

}
