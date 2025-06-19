package com.example.demo.dto;
import lombok.Data;
@Data
public class SongDTO {
    private Long id;
    private String name;
    private String artist;
    private String coverImgUrl;
    private Integer duration;
}

