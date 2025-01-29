package org.example.adds.Users;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.example.adds.Auth.*;
import org.example.adds.DraftUser.DraftUserRepo;
import org.example.adds.DraftUser.DraftUsers;
import org.example.adds.ExceptionHandlers.AllReadyExists;
import org.example.adds.Response;
import org.example.adds.Sms.SmsSender;
import org.example.adds.Wallet.Wallet;
import org.example.adds.Wallet.WalletRepo;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class UsersService {

    private final UsersRepo usersRepo;
    private final PasswordEncoder passwordEncoder;
    private final SmsSender smsSender;
    private final DraftUserRepo draftUserRepo;
    private final WalletRepo walletRepo;
    private final UsersMapper usersMapper;
    private static final int validPasswordLength = 6;

    @Transactional
    public void save(DraftUsers draftUser) {
        Users user = new Users();
        user.setFullName(draftUser.getFullName());
        user.setCompanyName(draftUser.getCompanyName());
        user.setPassword(passwordEncoder.encode(draftUser.getPassword()));
        user.setUserRole(Users.UserRole.ROLE_USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setPhone(draftUser.getPhone());
        usersRepo.save(user);
        /* when a user is signed up new virtual wallet is created for this user*/
        createWallet(user);
    }

    private void createWallet(Users user) {
        if (walletRepo.existsByUser(user)) {
            throw new AllReadyExists("wallet already exists");
        } else {
            Wallet wallet = new Wallet();
            wallet.setUser(user);
            wallet.setBalance(BigDecimal.valueOf(5000.00));
            wallet.setCreatedAt(LocalDateTime.now());
            wallet.setUpdatedAt(LocalDateTime.now());
            walletRepo.save(wallet);
        }
    }

    private boolean isPhoneValid(String phone) {
        final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("\\+998\\d{9}");
        return phone != null && !phone.isEmpty() && PHONE_NUMBER_PATTERN.matcher(phone).matches();
    }

    @Transactional
    public String saveDraftUser(SignUpRequest request) {

        if (!isPhoneValid(request.getPhone()) && !validPassword(request.getPassword())) {
            throw new IllegalArgumentException("Invalid phone or password format");
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

    private boolean validPassword(String password) {
        return password.length() >= validPasswordLength;
    }

    private String generateSmsCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }

    @Transactional
    public String verifyPhone(SigningRequest request) {
        DraftUsers draftUser = draftUserRepo.findByPhone(request.getPhone())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        boolean isPhoneAndCodeValid = draftUser.getPhone().equals(request.getPhone()) &&
                Objects.equals(draftUser.getSmsCode(), request.getSmsCode());

        if (isPhoneAndCodeValid) {
            if (LocalDateTime.now().isBefore(draftUser.getExpiresAt())) {
                save(draftUser);
                draftUserRepo.delete(draftUser);
                return "New user saved";
            } else {
                return resendSmsCode(draftUser, request.getPhone());
            }
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
                .orElseThrow(() -> new NoSuchElementException("data not found"));

        if (draftUser.getPhone().equals(dto.phone()) &&
                Objects.equals(draftUser.getSmsCode(), dto.smsCode())
                && LocalDateTime.now().isBefore(draftUser.getExpiresAt())) {

            draftUser.setSmsCode(null);
            draftUserRepo.save(draftUser);
            return "phone activated";

        } else if (Objects.equals(draftUser.getSmsCode(), dto.smsCode())
                && LocalDateTime.now().isAfter(draftUser.getExpiresAt())) {

            return resendSmsCode(draftUser, dto.phone());

        } else {
            throw new BadCredentialsException("Incorrect phone number or SMS code");
        }
    }

    @Transactional
    public String setNewPassword(SetNewPassword dto) {
        DraftUsers draftUser =
                draftUserRepo.findByPhone(dto.phone())
                        .orElseThrow(() -> new NoSuchElementException("user not found"));


        if (draftUser.getPhone().equals(dto.phone())
                && draftUser.getSmsCode() == null) {

            Users user = usersRepo.findByPhone(dto.phone())
                    .orElseThrow(() -> new NoSuchElementException("user not found"));

            user.setPassword(passwordEncoder.encode(dto.newPassword()));
            draftUserRepo.delete(draftUser);
            usersRepo.save(user);
            return "updated successfully";
        } else
            throw new BadCredentialsException("inactive phone");
    }

    public Users findById(UUID recipientId) {
        return usersRepo.findById(recipientId)
                .orElseThrow(() -> new NoSuchElementException("user not found"));
    }

    private String resendSmsCode(DraftUsers draftUser, String phone) {
        String smsCode = generateSmsCode();
        smsSender.sendSms(phone, smsCode);
        draftUser.setExpiresAt(LocalDateTime.now().plusMinutes(2));
        draftUser.setSmsCode(smsCode);
        draftUserRepo.save(draftUser);
        return "new password sent";
    }

    public String delete(String phone) {
        draftUserRepo.delete(draftUserRepo.findByPhone(phone)
                .orElseThrow(() -> new NoSuchElementException("not found")));
        return "deleted";
    }

    public Response resendSmsCodeToUser(Phone phone) {
        DraftUsers draftUser = draftUserRepo.findByPhone(phone.phone())
                .orElseThrow(() -> new NoSuchElementException("user with " + phone.phone() + " not found"));
        String s = resendSmsCode(draftUser, draftUser.getPhone());
        return new Response("sent successfully", true);
    }

    @Transactional
    public Response deleteByPhone(String phone) {
        Users user = usersRepo.findByPhone(phone)
                .orElseThrow(() -> new NoSuchElementException("user not found"));

        Wallet byUser = walletRepo.findByUser(user)
                .orElseThrow(() -> new NoSuchElementException("wallet not found"));

        walletRepo.delete(byUser);

        usersRepo.delete(user);
        return new Response("deleted", true);
    }

    public List<Users> getAllUsers() {
        return this.usersRepo.findAll();
    }

    public UsersDto getUserById(UUID userId) {
        return this.usersMapper.toDto(
                usersRepo.findById(userId)
                        .orElseThrow(() -> new NoSuchElementException("user not found")));
    }
}
