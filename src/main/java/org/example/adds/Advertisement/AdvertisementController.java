package org.example.adds.Advertisement;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.adds.Response;
import org.example.adds.Visitors.VisitorsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/adv")
@AllArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final VisitorsService visitorsService;

    @PostMapping("/create-link/{id}")
    public ResponseEntity<AdvLinkResponse> createAdv(@PathVariable UUID id,
                                       @RequestBody AdvLink request) {
        return ResponseEntity.ok(advertisementService.createAdv(id, request));
    }

    @PatchMapping("/update-status")
    public ResponseEntity<Response> updateStatus(@RequestBody UpdateStatus request){
        try{
            return ResponseEntity.ok(advertisementService.updateStatus(request));
        }catch (NoSuchElementException ex){
            return ResponseEntity.status(404).body(new Response(ex.getMessage(), false));
        }
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

    @GetMapping("/qr-code/{advId}")
    public ResponseEntity<byte[]> getQrCode(@PathVariable UUID advId) throws Exception {
        byte[] qrCodeImage = advertisementService.generateQrCode(advId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_PNG)
                .header("Content-Disposition", "inline; filename=\"qrcode.png\"")
                .body(qrCodeImage);
    }

    @GetMapping("/get-by/{userId}")
    public ResponseEntity<Page<AdvResponse>> getByUserId(
            @PathVariable UUID userId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(advertisementService.getByUserId(userId, pageable));
    }


    @PatchMapping("/edit") //edit by adv id
    public ResponseEntity<AdvResponse> editAdv(@RequestBody EditAdv request){
        return ResponseEntity.ok(advertisementService.editAdv(request));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Response> deleteAdv(@RequestBody DeleteRequest request){
        try {
            advertisementService.deleteAdv(request);
            return ResponseEntity.status(200).body(new Response("deleted successfully", true));
        }catch (NoSuchElementException ex){
            return ResponseEntity.status(404).body(new Response(ex.getMessage(), false));
        }
    }

    @GetMapping("/delete-view") // get method for deleting advertisement data
    public ResponseEntity<AdvDeleteView> deleteView(@RequestBody DeleteRequest request){
        try {
            return ResponseEntity.ok(advertisementService.deleteView(request));
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }
}
