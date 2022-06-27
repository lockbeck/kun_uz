package com.company.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "types")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TypesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true)
    private String key;
    @Column(nullable = false)
    private String nameUz;
    @Column(nullable = false)
    private String nameEn;
    @Column(nullable = false)
    private String nameRu;
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    @Column
    private Boolean visible = Boolean.TRUE;

    public TypesEntity(Integer id) {
        this.id = id;
    }

    public TypesEntity() {
    }
}
