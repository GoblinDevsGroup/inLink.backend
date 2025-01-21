package org.example.adds.Advertisement;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.example.adds.Visitors.VisitorsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

@RestController
@RequestMapping("/api/adv")
@AllArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final VisitorsService visitorsService;

    @PostMapping("/create-link/{id}")
    public ResponseEntity<?> createAdv(@PathVariable UUID id,
                                       @RequestBody AdvLink request) {
        return ResponseEntity.ok(advertisementService.createAdv(id, request));
    }

    @GetMapping("/get/{link}")
    public RedirectView redirectToMainLink(@PathVariable String link, HttpServletRequest request) {
        /*
        in order to count exact number of people who visited to the main link via adv link,
        it is better to handle user device IP which is unique for each device
         */
        String visitorIp = visitorsService.getClientIp(request);
        /* this is used to log user ip to db */
        visitorsService.logVisitors(link, visitorIp);
        String mainLink = advertisementService.redirectToMainLink(link);
        return new RedirectView(mainLink);
    }

    @GetMapping("/qr-code/{id}")
    public ResponseEntity<?> getQrCode(@PathVariable UUID id) throws Exception {
        byte[] qrCodeImage = advertisementService.generateQrCode(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_PNG)
                .header("Content-Disposition", "inline; filename=\"qrcode.png\"")
                .body(qrCodeImage);
    }

    @GetMapping("/get-by/{userId}")
    public ResponseEntity<?> getByUserId(@PathVariable UUID userId){
        return ResponseEntity.ok(advertisementService.getByUserId(userId));
    }

    @PatchMapping("/edit") //edit by adv id
    public ResponseEntity<?> editAdv(@RequestBody EditAdv request){
        return ResponseEntity.ok(advertisementService.editAdv(request));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAdv(@RequestBody DeleteRequest request){
        advertisementService.deleteAdv(request);
        return ResponseEntity.status(200).body("");
    }

    @GetMapping("/delete-view") // get method for deleting advertisement data
    public ResponseEntity<?> deleteView(@RequestBody DeleteRequest request){
        return ResponseEntity.ok(advertisementService.deleteView(request));
    }
}
