package com.example.demo.repository;

import com.example.demo.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository // 标记这是一个Spring管理的Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByNameContaining(String name);
}