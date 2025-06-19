package com.example.demo.dto;
import lombok.Data;
import java.util.List;

@Data
public class SearchResultDTO {
    private List<SongDTO> songs;
    private List<PlaylistDTO> playlists;
}
