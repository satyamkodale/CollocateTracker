package com.be.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String category;

//    @ElementCollection-> will create sepearte table for this
    private List<String> tags = new ArrayList<>();

    @ManyToOne
    private User owner;

    @JsonIgnore
    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL,orphanRemoval = true)
    private Chat chat;

    @JsonIgnore
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Issue> issues = new ArrayList<>();

    @ManyToMany
    private List<User> team = new ArrayList<>();

}


