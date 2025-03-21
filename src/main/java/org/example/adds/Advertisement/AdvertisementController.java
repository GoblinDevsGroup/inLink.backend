package org.example.adds.Advertisement;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.adds.Admin.Dtos.AdvResponseForAdmin;
import org.example.adds.Advertisement.Dto.*;
import org.example.adds.Response;
import org.example.adds.Visitors.VisitorsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/adv")
@AllArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final VisitorsService visitorsService;

    @PostMapping("/create-link/{userId}")
    public ResponseEntity<Response> createAdv(@PathVariable UUID userId,
                                       @RequestBody AdvLink request) {
        AdvLinkResponse adv = advertisementService.createAdv(userId, request);
        if (adv != null) {
            return ResponseEntity.ok(new Response("created successfully", true));
        }
        return ResponseEntity.ok(new Response("error in creating", false));
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

    @GetMapping("/get-by/{userId}")
    public ResponseEntity<Page<AdvResponseForAdmin>> getAdvByUserIdWithSearchingAndPageable(
            @PathVariable UUID userId,
            @RequestParam(value = "searchText", required = false) String searchText,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(advertisementService.getAdvByUserIdWithSearchingAndPageable(userId,searchText, pageable));
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

    @GetMapping("/get-one") // get method for deleting advertisement data
    public ResponseEntity<AdvDeleteView> deleteView(@RequestBody DeleteRequest request){
        try {
            return ResponseEntity.ok(advertisementService.deleteView(request));
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }
}