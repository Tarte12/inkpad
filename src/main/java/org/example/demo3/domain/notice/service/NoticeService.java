package org.example.demo3.domain.notice.service;

import com.alibaba.excel.EasyExcel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.demo3.domain.notice.Importance;
import org.example.demo3.domain.notice.Notice;
import org.example.demo3.domain.notice.dto.NoticeExcelRow;
import org.example.demo3.domain.notice.dto.NoticeResponseDto;
import org.example.demo3.domain.notice.dto.NoticeUpdateDto;
import org.example.demo3.domain.notice.repository.NoticeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

     //엑셀 업로드 시 기존 데이터 초기화 후 일괄 저장
    @Transactional
    public void saveAllFromExcel(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            // 1. 엑셀 파싱
            List<NoticeExcelRow> rows = EasyExcel.read(inputStream)
                    .head(NoticeExcelRow.class)
                    .sheet()
                    .doReadSync();

            // 2. 기존 데이터 초기화
            noticeRepository.deleteAllInBatch();

            // 3. 변환 및 저장
            List<Notice> notices = rows.stream()
                    .map(this::convertToEntity)
                    .collect(Collectors.toList());

            noticeRepository.saveAll(notices);
            log.info("✅ 총 {}건 공지사항이 새로 업로드되었습니다.", notices.size());
        }
    }

    /**
     * ✅ 엑셀 row → Notice entity 변환
     */
    private Notice convertToEntity(NoticeExcelRow row) {
        return Notice.builder()
                .title(row.getTitle())
                .content(row.getContent())
                .importance(Importance.valueOf(row.getImportance().trim().toUpperCase())) // trim + 대문자화
                .publishedAt(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * ✅ 파싱된 결과를 Notice 엔티티로 변환하여 DB에 저장
     */
    public void saveAll(List<NoticeExcelRow> rows) {
        LocalDate now = LocalDate.now();
        LocalDateTime createdAt = LocalDateTime.now();

        List<Notice> notices = rows.stream()
                .map(row -> Notice.builder()
                        .title(row.getTitle())
                        .content(row.getContent())
                        .importance(Importance.valueOf(row.getImportance().trim().toUpperCase()))
                        .publishedAt(now)
                        .createdAt(createdAt)
                        .build())
                .collect(Collectors.toList());

        noticeRepository.saveAll(notices);
    }

    //공지사항 목록 조회
    public Page<NoticeResponseDto> getNoticeList(Pageable pageable) {
        return noticeRepository.findAll(pageable)
                .map(NoticeResponseDto::from);
    }

    //공지사항 상세 조회
    public NoticeResponseDto getNoticeById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항이 존재하지 않습니다."));
        return NoticeResponseDto.from(notice);
    }

    //공지사항 단건 수정
    @Transactional
    public void updateNotice(Long id, NoticeUpdateDto dto) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항이 존재하지 않습니다."));

        notice.update(dto.getTitle(), dto.getContent(), dto.getImportance());
    }

    //공지사항 단건 삭제
    @Transactional
    public void deleteNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항이 존재하지 않습니다."));
        noticeRepository.delete(notice);
    }
}




