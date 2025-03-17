//package org.example.adds.QRcode;
//
//import lombok.AllArgsConstructor;
//import org.example.adds.Advertisement.Advertisement;
//import org.example.adds.Advertisement.AdvertisementRepo;
//import org.springframework.stereotype.Service;
//
//import java.util.NoSuchElementException;
//import java.util.UUID;
//
//@Service
//@AllArgsConstructor
//public class QrCodeService {
//
//    private final AdvertisementRepo advertisementRepo;
//
//    public byte[] generateQrCode(UUID id) throws Exception {
//        Advertisement adv = advertisementRepo.findById(id)
//                .orElseThrow(() -> new NoSuchElementException("link not found"));
//
//        String advLink = adv.getAdvLink();
//
//        byte[] qrCode = QrCodeGenerator.generateQRCodeImageAsByteArray(advLink);
//        adv.setQrCode(qrCode);
//        advertisementRepo.save(adv);
//        return qrCode;
//    }
//
//    public byte[] getQrCode(UUID advId) {
//        Advertisement adv = advertisementRepo.findById(advId)
//                .orElseThrow(() -> new NoSuchElementException("Advertisement not found"));
//
//        return adv.getQrCode();
//    }
//}
