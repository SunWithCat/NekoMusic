package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "songs")
public class Song {

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    private String artist;

    private String coverImgUrl;

    private Integer duration;
}