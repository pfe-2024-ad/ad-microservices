package com.eai.client_service.controller;

import com.eai.client_service.dto.ClientResponseDto;
import com.eai.client_service.model.Client;
import com.eai.client_service.service.OcrService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("api/get-infos-cin")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
public class OcrController {

    private final OcrService ocrService;

    @PostMapping
    public ClientResponseDto getInfosCIN(@RequestParam("image") MultipartFile[] files ){

        return ocrService.getInfosCIN(files);
    }

}
