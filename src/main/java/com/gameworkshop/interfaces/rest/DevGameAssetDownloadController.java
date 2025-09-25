package com.gameworkshop.interfaces.rest;

import com.gameworkshop.application.service.DevGameAssetApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class DevGameAssetDownloadController {
    private final DevGameAssetApplicationService devGameAssetApplicationService;

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable("id") String id) {
        return devGameAssetApplicationService.loadGameAssetForDownload(id)
                .filter(devGameAsset -> new File(devGameAsset.getStoragePath()).exists())
                .map(devGameAsset -> {
                    File file = new File(devGameAsset.getStoragePath());
                    Resource resource = new FileSystemResource(file);
                    ContentDisposition cd = ContentDisposition.attachment()
                            .filename(devGameAsset.getFileName(), StandardCharsets.UTF_8)
                            .build();
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, cd.toString())
                            //这里也是不适用数据库存储的信息而是读取磁盘，保证绝对安全
                            .contentType(MediaType.APPLICATION_OCTET_STREAM)
                            //这里用file.length而不是用数据库的方法，是因为保证绝对准确，不会因数据字段大小不匹配导致下载失败，缺点是有一定开销，他会直接读取磁盘，但是准确
                            .contentLength(file.length())
                            .body(resource);

                })
                .orElse(ResponseEntity.notFound().build());
    }

}
