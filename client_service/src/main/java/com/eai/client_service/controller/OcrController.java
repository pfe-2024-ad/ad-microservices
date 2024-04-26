package com.eai.client_service.controller;

import com.eai.client_service.dto.mocks.ocr.ClientResponseOcrDto;
import com.eai.client_service.service.OcrService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/get-cin-infos")
@RequiredArgsConstructor

public class OcrController {

    private final OcrService ocrService;

    @PostMapping
    public Object getCinInfos(@RequestParam MultipartFile file1, @RequestParam MultipartFile file2, @RequestParam MultipartFile file3, @RequestParam Integer id){

        return ocrService.getCinInfos(file1, file2, file3, id);
    }

}
