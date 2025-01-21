package org.example.adds.Users;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.example.adds.Auth.SetNewPassword;
import org.example.adds.Auth.SignUpRequest;
import org.example.adds.Auth.SigningRequest;
import org.example.adds.Auth.VerifyForgotPassword;
import org.example.adds.DraftUser.DraftUserRepo;
import org.example.adds.DraftUser.DraftUsers;
import org.example.adds.Sms.SmsSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class UsersService {

    private final UsersRepo usersRepo;
    private final PasswordEncoder passwordEncoder;
    private final SmsSender smsSender;
    private final DraftUserRepo draftUserRepo;

    public Users save(DraftUsers draftUser) {
        Users user = new Users();
        user.setFullName(draftUser.getFullName());
        user.setCompanyName(draftUser.getCompanyName());
        user.setPassword(passwordEncoder.encode(draftUser.getPassword()));
        user.setUserRole(Users.UserRole.ROLE_USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setPhone(draftUser.getPhone());
        return usersRepo.save(user);
    }

    private boolean isPhoneValid(String phone) {
        final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("\\+998\\d{9}");
        return phone != null && !phone.isEmpty() && PHONE_NUMBER_PATTERN.matcher(phone).matches();
    }

    @Transactional
    public String saveDraftUser(SignUpRequest request) {

        if (!isPhoneValid(request.getPhone())) {
            throw new IllegalArgumentException("Invalid phone number format");
        }

        DraftUsers user = new DraftUsers();

        user.setFullName(request.getFullName());
        user.setCompanyName(request.getCompanyName());
        user.setPhone(request.getPhone());
        user.setPassword(request.getPassword());
        user.setCreatedAt(LocalDateTime.now());
        String smsCode = generateSmsCode();

        user.setSmsCode(smsCode);
        user.setExpiresAt(LocalDateTime.now().plusMinutes(2));
        draftUserRepo.save(user);
        smsSender.sendSms(request.getPhone(), smsCode);
        return "SMS code sent";
    }

    private String generateSmsCode() {
        return String.format("%04d", new Random().nextInt(10000)); // Always generates a 4-digit number
    }

    @Transactional
    @SneakyThrows
    public Users verifyPhone(SigningRequest request) {

        DraftUsers draftUser = draftUserRepo.findByPhone(request.getPhone())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (draftUser.getPhone().equals(request.getPhone()) &&
                Objects.equals(draftUser.getSmsCode(), request.getSmsCode())
        && LocalDateTime.now().isBefore(draftUser.getExpiresAt())) {
            Users user = save(draftUser);
            draftUserRepo.delete(draftUser);
            return user;
        } else {
            throw new BadCredentialsException("Incorrect phone number or SMS code");
        }
    }

    @Transactional
    public String forgotPassword(String phone) {
        Users user = usersRepo.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("User with phone number " + phone + " not found"));

        DraftUsers draftUser = new DraftUsers();

        draftUser.setFullName(user.getFullName());
        draftUser.setCompanyName(user.getCompanyName());
        draftUser.setPhone(user.getPhone());
        draftUser.setCreatedAt(LocalDateTime.now());

        String newSmsCode = generateSmsCode();
        draftUser.setSmsCode(newSmsCode);
        draftUser.setExpiresAt(LocalDateTime.now().plusMinutes(2));
        draftUserRepo.save(draftUser);
        smsSender.sendSms(phone, newSmsCode);

        return "New SMS has been sent";
    }

    public String activatePhone(VerifyForgotPassword dto) {
        DraftUsers draftUser = draftUserRepo.findByPhone(dto.phone())
                .orElseThrow(()->new NoSuchElementException("data not found"));

        if (draftUser.getPhone().equals(dto.phone()) &&
                Objects.equals(draftUser.getSmsCode(), dto.smsCode())
        && LocalDateTime.now().isBefore(draftUser.getExpiresAt())) {
            draftUser.setSmsCode(null);
            draftUserRepo.save(draftUser);
            return "phone activated";
        } else {
            throw new BadCredentialsException("Incorrect phone number or SMS code");
        }
    }

    @Transactional
    public Users setNewPassword(SetNewPassword dto) {
        DraftUsers draftUser =
                draftUserRepo.findByPhone(dto.phone())
                        .orElseThrow(()->new NoSuchElementException("user not found"));

        if (draftUser.getPhone().equals(dto.phone())
                && draftUser.getSmsCode() == null){

            Users user = usersRepo.findByPhone(dto.phone())
                    .orElseThrow(()->new NoSuchElementException("user not found"));

            user.setPassword(passwordEncoder.encode(dto.newPassword()));
            draftUserRepo.delete(draftUser);
            return usersRepo.save(user);
        }
        else
            throw new RuntimeException("inactive phone");
    }
}
