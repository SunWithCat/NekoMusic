package com.example.demo.repository;

import com.example.demo.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    // 根据歌曲名或歌手名模糊查询
    List<Song> findByNameContainingOrArtistContaining(String name, String artist);
}