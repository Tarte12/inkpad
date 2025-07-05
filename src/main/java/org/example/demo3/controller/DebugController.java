package org.example.demo3.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value; // ✅ 이게 맞는 import
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/debug")
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
}
