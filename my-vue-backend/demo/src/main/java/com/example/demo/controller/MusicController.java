package com.example.demo.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api") // 给这个Controller的所有接口地址加上 /api 前缀
public class MusicController {

    // 定义音乐文件存放的基础路径
    private final Path musicStorageLocation = Paths.get("src/main/resources/music").toAbsolutePath().normalize();

    @GetMapping("/music/{songId}") // 定义接口地址，{songId} 是一个占位符
    public ResponseEntity<Resource> getMusicFile(@PathVariable String songId) {
        try {
            Path filePath = this.musicStorageLocation.resolve(songId + ".mp3").normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // 设置响应头，告诉浏览器这是一个音频文件流
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType("audio/mpeg")) // audio/mpeg 对应 .mp3 文件
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                // 如果文件不存在，返回 404 Not Found
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
}