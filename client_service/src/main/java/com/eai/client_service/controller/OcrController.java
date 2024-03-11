package com.eai.client_service.controller;

import com.eai.client_service.dto.mocks.ocr.ClientResponseOcrDto;
import com.eai.client_service.service.OcrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/get-infos-cin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class OcrController {

    private final OcrService ocrService;

    @PostMapping
    public ClientResponseOcrDto getInfosCIN(@RequestParam MultipartFile file1, @RequestParam MultipartFile file2, @RequestParam MultipartFile file3, @RequestParam Integer id){

        return ocrService.getInfosCIN(file1, file2, file3, id);
    }

}
