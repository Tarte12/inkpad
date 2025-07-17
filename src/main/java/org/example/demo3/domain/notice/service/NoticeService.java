package org.example.demo3.domain.notice.service;

import lombok.RequiredArgsConstructor;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    /**
     * ✅ 엑셀 파싱 결과를 Notice 엔티티로 변환하여 DB에 저장
     */
    public void saveAll(List<NoticeExcelRow> rows) {
        LocalDate now = LocalDate.now();  // 오늘 날짜 기준 게시일
        LocalDateTime createdAt = LocalDateTime.now();

        List<Notice> notices = rows.stream()
                .map(row -> Notice.builder()
                        .title(row.title()) // ✅ record용 accessor
                        .content(row.content())
                        .importance(Importance.valueOf(row.importance().toUpperCase()))
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

        notice.update(dto.title(), dto.content(), dto.importance());
    }

}



