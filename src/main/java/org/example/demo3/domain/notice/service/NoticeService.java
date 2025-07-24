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
import org.example.demo3.global.exception.BlogException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.example.demo3.global.exception.ErrorCode;
import org.example.demo3.domain.notice.validation.ExcelRowValidator;



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

             // 3. 유효성 검사
             for (NoticeExcelRow row : rows) {
                 ExcelRowValidator.validate(row);
             }

             // 4. 저장 + 로그 → 공통 메서드 호출
             saveAll(rows);

         } catch (BlogException e) {
             throw e;
         } catch (Exception e) {
             throw new BlogException(ErrorCode.INTERNAL_SERVER_ERROR);
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
     * ✅ 파싱된 결과를 Notice 엔티티로 변환하여 DB에 저장 -> 엔티티나 dto에 넣는 게 더 깔끔할 듯?
     * 이름 수정 <- 메서드 이름 수정 필요
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

        log.info("✅ 공지사항 업로드 시도: {}건, 성공: {}건", rows.size(), notices.size());
    }

    //공지사항 목록 조회
    //페이징 관련 DTO 만들기 <- 다른 페이징 기능도 필요
    //페이징 자체는 요청을 해야 함 몇 번째 페이지야? 몇 개의 페이지야? 오름차순이야 내림차순이야? 페이지 번호
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
        noticeRepository.delete(notice); //deleteByID로 바꾸면 더 좋을 듯
    }
}




