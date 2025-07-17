package org.example.demo3.domain.notice.controller;

import com.alibaba.excel.EasyExcel;
import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.notice.dto.NoticeExcelRow;
import org.example.demo3.domain.notice.dto.NoticeResponseDto;
import org.example.demo3.domain.notice.dto.NoticeUpdateDto;
import org.example.demo3.domain.notice.service.NoticeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    //공지사항 업로드
    //테스트 후 예외처리 만들어 줘야 함
// ✅ Controller
    @PostMapping("/upload")
    public ResponseEntity<Void> uploadExcel(@RequestParam("file") MultipartFile file) throws IOException {
        noticeService.saveAllFromExcel(file);
        return ResponseEntity.ok().build();
    }


    @GetMapping
    public ResponseEntity<Page<NoticeResponseDto>> getNotices(
            @PageableDefault(size = 10, sort = "publishedAt") Pageable pageable
    ) {
        return ResponseEntity.ok(noticeService.getNoticeList(pageable));
    }

    //공지사항 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<NoticeResponseDto> getNotice(@PathVariable Long id) {
        return ResponseEntity.ok(noticeService.getNoticeById(id));
    }

    //공지사항 단건 수정
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateNotice(
            @PathVariable Long id,
            @RequestBody NoticeUpdateDto dto
    ) {
        noticeService.updateNotice(id, dto);
        return ResponseEntity.ok().build();
    }

    //공지사항 단건 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }


}
