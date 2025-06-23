package com.example.demo.controller;

import com.example.demo.dto.PlaylistDTO;
import com.example.demo.dto.SearchResultDTO;
import com.example.demo.dto.SongDTO;
import com.example.demo.repository.PlaylistRepository;
import com.example.demo.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SearchController {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SongRepository songRepository;

    private PlaylistDTO toPlaylistDTO(com.example.demo.entity.Playlist playlist) {
        PlaylistDTO dto = new PlaylistDTO();
        dto.setId(playlist.getId());
        dto.setName(playlist.getName());
        dto.setPlayCount(playlist.getPlayCount());
        dto.setCoverImgUrl(playlist.getCoverImgUrl());
        return dto;
    }

    private SongDTO toSongDTO(com.example.demo.entity.Song song) {
        SongDTO dto = new SongDTO();
        dto.setId(song.getId());
        dto.setName(song.getName());
        dto.setArtist(song.getArtist());
        dto.setCoverImgUrl(song.getCoverImgUrl());
        dto.setDuration(song.getDuration());
        return dto;
    }

    // 改造 search 方法
    @GetMapping("/search")
    public SearchResultDTO search(@RequestParam(defaultValue = "") String query) {
        SearchResultDTO results = new SearchResultDTO();
        if (query.trim().isEmpty()) {
            results.setPlaylists(List.of()); // 返回空列表
            results.setSongs(List.of());
            return results;
        }

        // 从数据库搜索歌单
        List<PlaylistDTO> foundPlaylists = playlistRepository.findByNameContaining(query)
                .stream()
                .map(this::toPlaylistDTO) // Stream API, 将Entity转换为DTO
                .collect(Collectors.toList());

        // 从数据库搜索歌曲
        List<SongDTO> foundSongs = songRepository.findByNameContainingOrArtistContaining(query, query)
                .stream()
                .map(this::toSongDTO)
                .collect(Collectors.toList());

        results.setPlaylists(foundPlaylists);
        results.setSongs(foundSongs);
        return results;
    }
}