package com.company.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Setter
@Getter
@ToString
public class AuthDTO {
@NotBlank(message = "Email is null or empty MAZGI")
@Pattern(regexp = "^(.+)@(.+)$",message = "Email format error MAZGI")
    private String email;
@NotBlank(message = "password is null or empty MAZGI")
@Size(min = 3, max = 100,message = "Password length must be >=3 and =<100")
    private String password;
}
