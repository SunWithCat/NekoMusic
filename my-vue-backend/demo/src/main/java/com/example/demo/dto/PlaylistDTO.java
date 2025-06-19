package com.example.demo.dto;
import lombok.Data;

@Data
public class PlaylistDTO {
    private Long id;
    private String name;
    private Long playCount;
    private String coverImgUrl;
}
