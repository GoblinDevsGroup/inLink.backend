package org.example.adds.Advertisement;

import org.example.adds.Advertisement.Dto.AdvLinkResponse;
import org.example.adds.Users.Users;
import org.example.adds.Users.UsersRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdvertisementServiceTest {

    @Mock
    private AdvertisementRepo advertisementRepo;

    @Mock
    private UsersRepo usersRepo;

    @Mock
    private AdvMapper advMapper;

    @InjectMocks
    private AdvertisementService advertisementService;

    private UUID userId;
    private Users user;
    private AdvLink request;
    private Advertisement advertisement;

    private String baseLink;

    private String subLink;

    @BeforeEach
    void setUp() {
        /* these are invoked here so that these variables are used in all part of this test class*/
        userId = UUID.randomUUID();
        UUID advId = UUID.randomUUID();
        subLink = UUID.randomUUID().toString();
        user = new Users(userId,
                "islom",
                "tuit",
                "+998934583558",
                "password",
                Users.UserRole.ROLE_USER,
                LocalDateTime.now(),
                LocalDateTime.now());

        request = new AdvLink("Test Ad",
                "https://example.com");

        baseLink = "https://sculpin-golden-bluejay.ngrok-free.app/api/adv/get/";

        advertisement = new Advertisement(
                advId,
                request.title(),
                AdStatus.ACTIVE,
                baseLink + subLink,
                request.mainLink(),
                0,
                LocalDateTime.now(),
                user,
                new byte[]{1, 2, 3, 4, 5},
                LocalDateTime.now()
        );
    }


    @Test
    void testGenerateAdvLink() {
        String uuid = UUID.randomUUID().toString();
        String shortUuid = uuid.substring(0, 8);
        String link = baseLink + shortUuid;

        assertEquals(link, baseLink + shortUuid);
    }

    @Test
    void testGetAdvLinkBySubLink() {
        String advLink = baseLink + subLink;

        when(advertisementRepo.findByAdvLink(advLink))
                .thenReturn(Optional.of(advertisement));

        Advertisement result = advertisementService.getAdvLinkBySubLink(subLink);

        verify(advertisementRepo, times(1)).findByAdvLink(advLink);

        assertNotNull(result);
        assertEquals(advertisement.getId(), result.getId());
        assertEquals(advertisement.getStatus(), result.getStatus());
        assertEquals(advertisement.getAdvLink(), result.getAdvLink());
        assertEquals(advertisement.getMainLink(), result.getMainLink());
    }

    @Test
    void testCreateAdv_Success() {
        when(usersRepo.findById(userId)).thenReturn(Optional.of(user));

        when(advMapper.toEntity(any(AdvLink.class), eq(user), anyString()))
                .thenReturn(advertisement);

        when(advertisementRepo.save(any(Advertisement.class))).thenReturn(advertisement);

        AdvLinkResponse response = advertisementService.createAdv(userId, request);

        verify(usersRepo, times(1)).findById(userId);
        verify(advMapper, times(1)).toEntity(any(AdvLink.class), eq(user), anyString());
        verify(advertisementRepo, times(1)).save(any(Advertisement.class));

        assertNotNull(response);
        assertEquals(advertisement.getId(), response.getId());
        assertEquals(user.getId(), response.getUserId());
        assertEquals(advertisement.getTitle(), response.getTitle());
        assertEquals(advertisement.getAdvLink(), response.getAdvLink());
        assertEquals(advertisement.getMainLink(), response.getMainLink());
    }

    @Test
    void testCreateAdv_UserNotFound() {
        when(usersRepo.findById(userId)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () ->
                advertisementService.createAdv(userId, request)
        );

        assertEquals("user not found", exception.getMessage());
        verify(usersRepo, times(1)).findById(userId);
        verify(advertisementRepo, never()).save(any());
    }

    @Test
    void testGetAdvLinkBySubLink_AdvNotFound(){
        String advLink = baseLink + subLink;
        when(advertisementRepo.findByAdvLink(advLink))
                .thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> advertisementService.getAdvLinkBySubLink(subLink)
        );

        assertEquals("Link not found", exception.getMessage());
        verify(advertisementRepo, times(1)).findByAdvLink(advLink);
    }
}
