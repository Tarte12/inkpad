package org.example.demo3.domain.notice.service;

import org.example.demo3.domain.notice.Importance;
import org.example.demo3.domain.notice.Notice;
import org.example.demo3.domain.notice.dto.NoticeResponseDto;
import org.example.demo3.domain.notice.dto.NoticeUpdateDto;
import org.example.demo3.domain.notice.repository.NoticeRepository;
import org.example.demo3.global.exception.BlogException;
import org.example.demo3.global.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.InputStream;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NoticeServiceTest {

    private NoticeRepository noticeRepository;
    private NoticeService noticeService;

    @BeforeEach
    void setUp() {
        noticeRepository = mock(NoticeRepository.class);
        noticeService = new NoticeService(noticeRepository);
    }

    @Test
    @DisplayName("공지사항 상세조회 - 성공")
    void 공지사항_상세조회_성공() {
        // given
        Notice notice = Notice.builder()
                .title("title")
                .content("content")
                .importance(Importance.HIGH)
                .build();
        ReflectionTestUtils.setField(notice, "id", 1L);

        when(noticeRepository.findById(1L)).thenReturn(Optional.of(notice));

        // when
        NoticeResponseDto result = noticeService.getNoticeById(1L);

        // then
        assertThat(result.getTitle()).isEqualTo("title");
        assertThat(result.getImportance()).isEqualTo("HIGH");
        verify(noticeRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("공지사항 수정")
    void 공지사항_수정() {
        // given
        Notice notice = Notice.builder()
                .title("old title")
                .content("old content")
                .importance(Importance.NORMAL)
                .build();
        ReflectionTestUtils.setField(notice, "id", 1L);

        when(noticeRepository.findById(1L)).thenReturn(Optional.of(notice));

        NoticeUpdateDto dto = new NoticeUpdateDto("new title", "new content", Importance.HIGH);

        // when
        noticeService.updateNotice(1L, dto);

        // then
        assertThat(notice.getTitle()).isEqualTo("new title");
        assertThat(notice.getContent()).isEqualTo("new content");
        assertThat(notice.getImportance()).isEqualTo(Importance.HIGH);
        verify(noticeRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("공지사항 목록 페이징조회")
    void 공지사항_목록_페이징조회() {
        // given
        Notice notice = Notice.builder()
                .title("공지사항 제목")
                .content("내용")
                .importance(Importance.NORMAL)
                .build();
        ReflectionTestUtils.setField(notice, "id", 1L);

        Page<Notice> page = new PageImpl<>(Collections.singletonList(notice));
        PageRequest pageable = PageRequest.of(0, 10);

        when(noticeRepository.findAll(pageable)).thenReturn(page);

        // when
        Page<NoticeResponseDto> result = noticeService.getNoticeList(pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("공지사항 제목");
        verify(noticeRepository, times(1)).findAll(pageable);
    }

    @DisplayName("엑셀 업로드 - 유효한 파일은 성공")
    @Test
    void 엑셀_업로드_성공() throws Exception {
        // given
        InputStream is = new ClassPathResource("sample/valid_sample.xlsx").getInputStream();
        MockMultipartFile file = new MockMultipartFile(
                "file", "valid_sample.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", is
        );

        when(noticeRepository.saveAll(anyList())).thenReturn(Collections.emptyList());

        // when & then
        assertDoesNotThrow(() -> noticeService.saveAllFromExcel(file));
    }

    @DisplayName("엑셀 업로드 - 잘못된 중요도는 예외 발생")
    @Test
    void 엑셀_업로드_중요도_실패() throws Exception {
        // given
        InputStream is = new ClassPathResource("sample/invalid_importance.xlsx").getInputStream();
        MockMultipartFile file = new MockMultipartFile(
                "file", "invalid_importance.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", is
        );

        // when & then
        BlogException exception = assertThrows(BlogException.class, () -> {
            noticeService.saveAllFromExcel(file);
        });
        assertEquals(ErrorCode.INVALID_EXCEL_ROW, exception.getErrorCode());
        System.out.println("예외 메시지: " + exception.getMessage());
    }

    @DisplayName("엑셀 업로드 - 제목/내용/중요도 누락 시 예외 발생")
    @Test
    void 엑셀_업로드_누락_실패() throws Exception {
        // given
        InputStream is = new ClassPathResource("sample/invalid_blank.xlsx").getInputStream();
        MockMultipartFile file = new MockMultipartFile(
                "file", "invalid_blank.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", is
        );

        // when & then
        BlogException exception = assertThrows(BlogException.class, () -> {
            noticeService.saveAllFromExcel(file);
        });
        assertEquals(ErrorCode.INVALID_EXCEL_ROW, exception.getErrorCode());
        System.out.println("예외 메시지: " + exception.getMessage());
    }


}
