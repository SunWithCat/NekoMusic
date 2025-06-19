package com.example.demo.controller;

import com.example.demo.dto.PlaylistDTO;
import com.example.demo.dto.SearchResultDTO;
import com.example.demo.dto.SongDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SearchController {

    private final List<JsonNode> allPlaylists;

    // 在构造函数中加载一次数据
    public SearchController() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = TypeReference.class.getResourceAsStream("/mock-data.json");
            this.allPlaylists = mapper.readValue(inputStream, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("加载模拟数据失败", e);
        }
    }

    @GetMapping("/search")
    public SearchResultDTO search(@RequestParam(defaultValue = "") String query) {
        String lowerCaseQuery = query.toLowerCase();
        SearchResultDTO results = new SearchResultDTO();

        if (query.trim().isEmpty()) {
            results.setPlaylists(new ArrayList<>());
            results.setSongs(new ArrayList<>());
            return results;
        }

        // 搜索歌单
        List<PlaylistDTO> foundPlaylists = allPlaylists.stream()
            .filter(p -> p.get("name").asText().toLowerCase().contains(lowerCaseQuery))
            .map(p -> {
                PlaylistDTO dto = new PlaylistDTO();
                dto.setId(p.get("id").asLong());
                dto.setName(p.get("name").asText());
                dto.setCoverImgUrl(p.get("coverImgUrl").asText());
                dto.setPlayCount(p.get("playCount").asLong());
                return dto;
            })
            .collect(Collectors.toList());

        // 搜索歌曲
        List<SongDTO> foundSongs = new ArrayList<>();
        allPlaylists.forEach(p -> {
            if (p.has("songs")) {
                p.get("songs").forEach(s -> {
                    if (s.get("name").asText().toLowerCase().contains(lowerCaseQuery) ||
                        s.get("artist").asText().toLowerCase().contains(lowerCaseQuery)) {

                        SongDTO songDto = new SongDTO();
                        songDto.setId(s.get("id").asLong());
                        songDto.setName(s.get("name").asText());
                        songDto.setArtist(s.get("artist").asText());
                        songDto.setCoverImgUrl(s.get("coverImgUrl").asText());
                        songDto.setDuration(s.get("duration").asInt());
                        foundSongs.add(songDto);
                    }
                });
            }
        });

        results.setPlaylists(foundPlaylists);
        results.setSongs(foundSongs);
        return results;
    }
}