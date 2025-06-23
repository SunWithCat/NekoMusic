package com.example.demo.util;

import com.example.demo.entity.Playlist;
import com.example.demo.repository.PlaylistRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.util.List; 

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Override
    public void run(String... args) throws Exception {
        if (playlistRepository.count() == 0) {
            System.out.println("数据库为空，正在初始化数据...");
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            InputStream inputStream = TypeReference.class.getResourceAsStream("/mock-data.json");
            try {
                // 这里的 List<Playlist> 需要 java.util.List 的 import
                List<Playlist> playlists = mapper.readValue(inputStream, new TypeReference<>() {});
                playlistRepository.saveAll(playlists);
                System.out.println("数据初始化完成！");
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("初始化数据失败：" + e.getMessage());
            }
        } else {
            System.out.println("数据库中已有数据，跳过初始化。");
        }
    }
}