package org.example.adds.QRcode;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/qrcode")
@AllArgsConstructor
public class QrCodeController {

    private final QrCodeService qrCodeService;
    @GetMapping("/create/{advId}")
    public ResponseEntity<byte[]> getQrCode(@PathVariable UUID advId) throws Exception {
        byte[] qrCodeImage = qrCodeService.generateQrCode(advId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_PNG)
                .header("Content-Disposition",
                        "inline;" +
                                " filename=\"qrcode.png\"")
                .body(qrCodeImage);
    }

    @GetMapping("/download/{advId}")
    public ResponseEntity<byte[]> downloadQrCode(@PathVariable UUID advId) throws Exception {
        byte[] qrCode = qrCodeService.getQrCode(advId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_PNG)
                .header("Content-Disposition",
                        "attachment;" +
                                " filename=\"qrcode.png\"")
                .body(qrCode);
    }

}
