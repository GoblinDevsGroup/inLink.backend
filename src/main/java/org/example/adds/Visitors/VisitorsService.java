package org.example.adds.Visitors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.adds.Advertisement.AdStatus;
import org.example.adds.Advertisement.Advertisement;
import org.example.adds.Advertisement.AdvertisementService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class VisitorsService {

    private final VisitorsRepo visitorsRepo;
    private final AdvertisementService advertisementService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    @Transactional
    public void logVisitors(String subLink, String visitorIp) {
        Advertisement adv = advertisementService.getAdvLinkBySubLink(subLink);

        if (!visitorsRepo.existsByIp(visitorIp)) {
            Visitors visitor = new Visitors();
            visitor.setIp(visitorIp);
            visitor.setAdvertisement(adv);
            visitor.setVisitedAt(LocalDateTime.now());
            visitorsRepo.save(visitor);
            if (adv.getStatus().equals(AdStatus.ACTIVE)) {
                adv.setVisitorNumber(adv.getVisitorNumber() + 1);
            }
            simpMessagingTemplate.convertAndSendToUser(
                    adv.getUser().getPhone(),
                    "/panel/visitor/count",
                    adv
            );
        }
    }

}
