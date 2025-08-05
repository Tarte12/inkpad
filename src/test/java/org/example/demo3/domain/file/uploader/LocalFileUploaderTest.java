package org.example.demo3.domain.file.uploader;

import org.example.demo3.domain.file.FileType;
import org.example.demo3.global.util.ImageProcessUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LocalFileUploaderTest {

    private LocalFileUploader localFileUploader;

    @BeforeEach
    void setUp() {
        localFileUploader = new LocalFileUploader();
        // 수동으로 @Value 값 세팅
        localFileUploader.setUploadDir("src/test/resources/upload");
        localFileUploader.setLocalBaseUrl("http://localhost:8080/files");
        // 업로드 디렉토리 미리 생성
        new java.io.File("src/test/resources/upload").mkdirs();
    }

    @Test
    void 이미지파일_업로드_성공_WebP변환() throws Exception {
        // given
        BufferedImage testImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        byte[] jpegBytes = ImageProcessUtil.bufferedImageToBytes(testImage, "jpg");
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", jpegBytes);

        // when
        org.example.demo3.domain.file.File uploadedFile = localFileUploader.storeFile(multipartFile);

        // then
        //assetThat <- 이걸 더 많이 씀 (다른 라이브러리)
        //가독성이 더 좋음
        //외부 코드 상관 안 써도 되게 짜야 함 + 인터페이스 호출하는 느낌으로
        assertNotNull(uploadedFile);
        assertEquals("image/webp", uploadedFile.getContentType());
        assertTrue(uploadedFile.getStoredFilename().endsWith(".webp"));
        assertEquals(FileType.IMAGE, uploadedFile.getFileType());

        // 실제 파일이 로컬에 존재하는지 확인
        java.io.File savedFile = new java.io.File(uploadedFile.getFilePath());
        assertTrue(savedFile.exists());
        assertTrue(savedFile.length() > 0);
    }

    @Test
    void 엑셀파일_업로드_성공() throws IOException {
        // given
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", "test.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "엑셀파일더미".getBytes());

        // when
        org.example.demo3.domain.file.File uploadedFile = localFileUploader.storeFile(multipartFile);

        // then
        assertNotNull(uploadedFile);
        assertEquals(FileType.EXCEL, uploadedFile.getFileType());
        assertTrue(uploadedFile.getStoredFilename().endsWith(".xlsx"));

        java.io.File savedFile = new java.io.File(uploadedFile.getFilePath());
        assertTrue(savedFile.exists());
    }

    @Test
    void pdf파일_업로드_성공() throws IOException {
        // given
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", "sample.pdf", "application/pdf", "PDF dummy content".getBytes());

        // when
        org.example.demo3.domain.file.File uploadedFile = localFileUploader.storeFile(multipartFile);

        // then
        assertNotNull(uploadedFile);
        assertEquals(FileType.PDF, uploadedFile.getFileType());
        assertTrue(uploadedFile.getStoredFilename().endsWith(".pdf"));
    }

    @Test
    void 기타파일_업로드_성공() throws IOException {
        // given
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", "readme.txt", "text/plain", "텍스트 파일".getBytes());

        // when
        org.example.demo3.domain.file.File uploadedFile = localFileUploader.storeFile(multipartFile);

        // then
        assertNotNull(uploadedFile);
        assertEquals(FileType.OTHER, uploadedFile.getFileType());
        assertTrue(uploadedFile.getStoredFilename().endsWith(".txt"));
    }
}
