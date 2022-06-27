package com.company.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TypesPaginationDTO {
    private Long total;
     private List<TypesDTO> list;
}
