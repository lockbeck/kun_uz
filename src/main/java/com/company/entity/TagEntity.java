package com.company.entity;

import com.company.enums.TagStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tag")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(name = "status",nullable = false)
    @Enumerated (EnumType.STRING)
    private TagStatus status;
    @Column(name = "created_date",nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    public TagEntity(String name) {
        this.name = name;
    }

    public TagEntity() {
    }
}
