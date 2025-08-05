package org.example.demo3.domain.notice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.notice.dto.NoticeResponseDto;
import org.example.demo3.domain.notice.dto.NoticeUpdateDto;
import org.example.demo3.domain.notice.service.NoticeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "Notice", description = "공지사항 업로드 및 관리 API")
@RestController
@RequestMapping("/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "공지사항 Excel 업로드",
            description = "관리자만 사용 가능. Excel(.xlsx) 파일을 업로드하여 기존 데이터를 초기화하고 새로 저장함",
            security = @SecurityRequirement(name = "bearerAuth")
    )
// ✅ Controller
    @PostMapping("/upload")
    public ResponseEntity<Void> uploadExcel(
            @Parameter(description = "업로드할 Excel 파일", required = true)
            @RequestParam("file") MultipartFile file) throws IOException {
        noticeService.saveAllFromExcel(file);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "공지사항 목록 조회", description = "공지사항 리스트를 페이지네이션 형태로 조회")
    @GetMapping
    public ResponseEntity<Page<NoticeResponseDto>> getNotices(
            @PageableDefault(size = 10, sort = "publishedAt") Pageable pageable
    ) {
        return ResponseEntity.ok(noticeService.getNoticeList(pageable));
    }

    @Operation(summary = "공지사항 단건 조회", description = "ID를 통해 공지사항 상세 내용을 조회")
    @GetMapping("/{id}")
    public ResponseEntity<NoticeResponseDto> getNotice(@PathVariable Long id) {
        return ResponseEntity.ok(noticeService.getNoticeById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "공지사항 단건 수정", description = "관리자만 사용 가능, ID를 통해 공지사항 내용을 수정")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateNotice(
            @PathVariable Long id,
            @RequestBody NoticeUpdateDto dto
    ) {
        noticeService.updateNotice(id, dto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "공지사항 단건 삭제", description = "관리자만 사용 가능, ID를 통해 공지사항을 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }


}
