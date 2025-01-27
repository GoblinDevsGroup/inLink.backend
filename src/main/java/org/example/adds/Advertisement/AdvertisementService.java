package org.example.adds.Advertisement;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.coyote.BadRequestException;
import org.example.adds.ExceptionHandlers.LinkExpiredException;
import org.example.adds.QRcode.QrCodeGenerator;
import org.example.adds.Response;
import org.example.adds.Users.Users;
import org.example.adds.Users.UsersRepo;
import org.example.adds.Wallet.TransactionRepo;
import org.example.adds.Wallet.TransactionService;
import org.example.adds.Wallet.Wallet;
import org.example.adds.Wallet.WalletService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
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
    private final TransactionService transactionService;

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

//        if (adv.getExpiresAt().isBefore(LocalDateTime.now())) {
//            throw new LinkExpiredException("Link expired");
//        }

        return adv.getMainLink();
    }

    public Advertisement getAdvLinkBySubLink(String subLink) {
        String advLink = baseLink + subLink;
        return advertisementRepo.findByAdvLink(advLink)
                .orElseThrow(() -> new NoSuchElementException("Link not found"));
    }

    public byte[] generateQrCode(UUID id) throws Exception {
        Advertisement adv = advertisementRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("link not found"));

        String advLink = adv.getAdvLink();
//        if (adv.getExpiresAt().isBefore(LocalDateTime.now())) {
//            throw new LinkExpiredException("Link expired");
//        }

        return QrCodeGenerator.generateQRCodeImageAsByteArray(advLink);
    }

    public List<AdvResponse> getByUserId(UUID userId) {

        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("user not found"));

        List<Advertisement> adv = advertisementRepo.findByUser(user);
        adv.sort(Comparator.comparing(Advertisement::getCreatedAt).reversed());

        return mapper.advertisementListToAdvResponseList(adv);
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
