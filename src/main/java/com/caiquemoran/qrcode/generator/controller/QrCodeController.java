package com.caiquemoran.qrcode.generator.controller;

import com.caiquemoran.qrcode.generator.dto.QrCodeGeneratorRequest;
import com.caiquemoran.qrcode.generator.dto.QrCodeGeneratorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.caiquemoran.qrcode.generator.service.QrCodeGeneratorService;


@RestController
@RequestMapping("/qrcode")
public class QrCodeController {

    private final QrCodeGeneratorService qrCodeGeneratorService;

    public QrCodeController(QrCodeGeneratorService qrCodeGeneratorService){
        this.qrCodeGeneratorService = qrCodeGeneratorService;
    }

    @PostMapping
    public ResponseEntity<QrCodeGeneratorResponse> generate(@RequestBody QrCodeGeneratorRequest request){
        
        try {
        QrCodeGeneratorResponse response = this.qrCodeGeneratorService.generateAndUploadQrCode(request.text());
        return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    
    }
}
