package org.example.demo3.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value; // ✅ 이게 맞는 import
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/debug")
public class DebugController {
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @GetMapping("/aws")
    public ResponseEntity<?> checkAwsEnv() {
        Map<String, String> result = new HashMap<>();
        result.put("accessKey", accessKey);
        result.put("secretKey", secretKey);
        result.put("bucket", bucket);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/multipart")
    public ResponseEntity<?> testMultipart(
            @RequestPart("post") String postJson,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        System.out.println("📦 POST JSON: " + postJson);
        System.out.println("🖼️ FILE COUNT: " + (files != null ? files.size() : 0));

        return ResponseEntity.ok().body(Map.of(
                "receivedJson", postJson,
                "fileCount", (files != null ? files.size() : 0)
        ));
    }
}
