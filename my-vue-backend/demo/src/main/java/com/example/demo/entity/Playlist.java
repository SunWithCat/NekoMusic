package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity // 告诉JPA，这是一个实体类，对应数据库中的一张表
@Table(name = "playlists") // 指定表名为 "playlists"
public class Playlist {

    @Id // 标记这是主键
    private Long id;

    @Column(nullable = false) // 不能为空
    private String name;

    private Long playCount;

    private String coverImgUrl;

    // 一个歌单(Playlist)包含多首歌曲(Song)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id") // 在songs表中创建一个外键
    private List<Song> songs;
}