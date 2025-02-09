package org.example.adds.Advertisement;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.adds.Advertisement.Dto.*;
import org.example.adds.ExceptionHandlers.PermissionDenied;
import org.example.adds.Response;
import org.example.adds.Users.Users;
import org.example.adds.Users.UsersRepo;
import org.example.adds.Wallet.WalletService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AdvertisementService {

    private final AdvertisementRepo advertisementRepo;
    private final UsersRepo usersRepo;
    private final AdvMapper mapper;
    private final WalletService walletService;
    private final static String baseLink = "https://sculpin-golden-bluejay.ngrok-free.app/api/adv/get/";


    public AdvLinkResponse createAdv(UUID id, AdvLink request) {

        Users user = usersRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("user not found"));

        String advLink = generateAdvLink();

        Advertisement adv = mapper.toEntity(request, user, advLink);

        UUID advId = advertisementRepo.save(adv).getId();

        return new AdvLinkResponse(
                advId,
                user.getId(),
                adv.getTitle(),
                adv.getAdvLink(),
                adv.getMainLink()
        );
    }

    private String generateAdvLink() {
        String uuid = UUID.randomUUID().toString();
        String shortUuid = uuid.substring(0, 8);
        return baseLink + shortUuid;
    }

    public String redirectToMainLink(String subLink) {

        Advertisement adv = getAdvLinkBySubLink(subLink);

        //todo: implement a logic to check link is active or inactive

        return adv.getMainLink();
    }

    public Advertisement getAdvLinkBySubLink(String subLink) {
        String advLink = baseLink + subLink;
        return advertisementRepo.findByAdvLink(advLink)
                .orElseThrow(() -> new NoSuchElementException("Link not found"));
    }

    public Page<AdvResponse> getAdvByUserIdWithSearchingAndPageable(UUID userId,
                                                                    String searchText,
                                                                    Pageable pageable) {
        Specification<Advertisement> spec = Specification
                .where(AdvertisementRepo.searchSpecification(searchText))
                .and(AdvertisementRepo.hasUserId(userId));

        Page<Advertisement> advPage = advertisementRepo.findAll(spec, pageable);

        return advPage.map(mapper::toResponse);
    }


    public AdvResponse editAdv(EditAdv request) {
        Advertisement adv = advertisementRepo.findById(request.advId())
                .orElseThrow(() -> new NoSuchElementException("adv not found"));

        if (adv.getUser().getId().equals(request.userId())) {
            adv.setTitle(request.title());
            adv.setUpdatedAt(LocalDateTime.now());
            advertisementRepo.save(adv);

            return mapper.toResponse(adv);
        } else
            throw new RuntimeException("permission denied");
    }

    public void deleteAdv(DeleteRequest request) {
        Advertisement adv = advertisementRepo.findById(request.advId())
                .orElseThrow(() -> new NoSuchElementException("adv not found"));

        if (adv.getUser().getId().equals(request.userId())) {
            advertisementRepo.delete(adv);
        }
    }

    public AdvDeleteView deleteView(DeleteRequest request) throws BadRequestException {
        Advertisement adv = advertisementRepo.findById(request.advId())
                .orElseThrow(() -> new NoSuchElementException("adv not found"));

        if (adv.getUser().getId().equals(request.userId())) {
            return new AdvDeleteView(
                    adv.getId(),
                    adv.getTitle(),
                    adv.getAdvLink(),
                    adv.getMainLink()
            );
        } else
            throw new BadRequestException("permission denied");
    }

    @Transactional
    public Response updateStatus(UpdateStatus request) {
        if (request == null) {
            throw new IllegalArgumentException("Invalid request");
        }

        Advertisement adv = advertisementRepo.findById(request.advId())
                .orElseThrow(() -> new NoSuchElementException("Advertisement not found"));

        Users user = adv.getUser();

        if (!user.getId().equals(request.userId())) {
            throw new PermissionDenied("Permission denied");
        }

        return updateAdvertisementStatus(adv, user, request.status());
    }

    private Response updateAdvertisementStatus(Advertisement adv, Users user, AdStatus status) {
        LocalDateTime now = LocalDateTime.now();
        adv.setUpdatedAt(now);

        return switch (status) {
            case ACTIVE -> activateAdvertisement(adv, user);
            case INACTIVE -> inactivateAdvertisement(adv);
            default -> throw new IllegalArgumentException("Invalid status value");
        };
    }

    private Response activateAdvertisement(Advertisement adv, Users user) {
        if (!walletService.checkUserWallet(user)) {
            return new Response("Charging failed. Please check your balance", false);
        }

        adv.setStatus(AdStatus.ACTIVE);
        advertisementRepo.save(adv);
        walletService.chargeFromWallet(user, adv.getTitle());

        return new Response("Activated successfully", true);
    }

    private Response inactivateAdvertisement(Advertisement adv) {
        adv.setStatus(AdStatus.INACTIVE);
        advertisementRepo.save(adv);
        return new Response("Inactivated successfully", true);
    }

    public List<AdvResponse> getAllAdv() {
        return this.advertisementRepo.findAll().stream()
                .map(this.mapper::toResponse)
                .toList();
    }
}
