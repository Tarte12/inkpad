package org.example.demo3.domain.file.controller;

import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.file.File;
import org.example.demo3.domain.file.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    //새 파일 생성 저장
    @PostMapping
    public ResponseEntity<File> create(@RequestBody File file){

        return  ResponseEntity.ok(fileService.create(file));
    }

    //모든 파일 조회
    @GetMapping
    public  ResponseEntity<List<File>> findAll() {

        return  ResponseEntity.ok(fileService.findAll());
    }

    //단건 파일 조회
    @GetMapping("/{id}")
    public ResponseEntity<File> findById(@PathVariable Long id){

        return  fileService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //파일 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){

        fileService.delete(id);
        return  ResponseEntity.noContent().build();
    }

    //파일 이름만 수정
    @PutMapping("/{id}/filename")
    public ResponseEntity<Void> updateFilename(@PathVariable Long id, @RequestBody Map<String, String> req) {
        String newFilename = req.get("filename");
        fileService.updateFilename(id, newFilename);
        return ResponseEntity.ok().build();
    }


    //파일 교체
    @PutMapping("{id}")
    public ResponseEntity<File> replace(@PathVariable Long id, @RequestBody File file){
        fileService.delete(id);
        File newFile = fileService.create(file);
        return  ResponseEntity.ok(newFile);
    }
}
