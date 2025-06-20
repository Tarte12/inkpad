package org.example.demo3.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.file.File;
import org.example.demo3.repository.FileRepository;
import org.springframework.stereotype.Service;
//실제 파일 업로드를 위해 추가

import java.util.List;
import java.util.Optional;
//파일 처리 예외를 위해 추가
//물리 파일 처리를 위해 추가


@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    //새 파일 생성(저장)
    public File create(File file){

        return  fileRepository.save(file);
    }

    //전체 조회
    public List<File> findAll(){

        return fileRepository.findAll();
    }

    //단건 조회
    public Optional<File> findById(Long id){

        return  fileRepository.findById((id));
    }

    //삭제
    public  void delete(Long id){

        fileRepository.deleteById(id);
    }

    //어떻게 호출할지 처리하는 메서드
    @Transactional
    public  void updateFilename(Long id, String newFilename) {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 파일 없음"));
        file.updateFilename(newFilename);
    }

}
