package org.example.adds.Advertisement;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.apache.coyote.BadRequestException;
import org.example.adds.QRcode.QrCodeGenerator;
import org.example.adds.Response;
import org.example.adds.Users.Users;
import org.example.adds.Users.UsersRepo;
import org.example.adds.Transactions.TransactionService;
import org.example.adds.Wallet.WalletService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public Page<AdvResponse> getByUserId(UUID userId, String searchText, Pageable pageable) {
        Specification<Advertisement> spec = Specification.where(AdvertisementRepo.searchSpecification(searchText))
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
            return new Response("Invalid request", false);
        }

        Advertisement adv = advertisementRepo.findById(request.advId())
                .orElseThrow(() -> new NoSuchElementException("Advertisement not found"));


        Users user = usersRepo.findById(request.userId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));


        if (!adv.getUser().equals(user)) {
            return new Response("Permission denied", false);
        }

        if (request.status().equals(AdStatus.ACTIVE)) {
            boolean result = walletService.checkUserWallet(user);

            if (result) {
                adv.setStatus(AdStatus.ACTIVE);
                adv.setUpdatedAt(LocalDateTime.now());
                advertisementRepo.save(adv);
                walletService.chargeFromWallet(user, adv.getTitle());
                return new Response("activated successfully", true);
            } else {
                return new Response("Charging failed. Please check your balance", false);
            }
        }

        if (request.status().equals(AdStatus.INACTIVE)) {
            adv.setStatus(AdStatus.INACTIVE);
            adv.setUpdatedAt(LocalDateTime.now());
            //todo: method to not charge from a user wallet for this advertisement since the user is making it inactive
            advertisementRepo.save(adv);
            return new Response("inactivated successfully", true);
        }

        return new Response("Invalid status value", false);
    }
}
