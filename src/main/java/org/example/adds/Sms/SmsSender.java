package org.example.adds.Sms;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Contract;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
//@AllArgsConstructor
@PropertySource("classpath:application.properties")
public class SmsSender {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${sms.url}")
    private String smsServiceUrl;

    @Value("${sms.email}")
    private String email;

    @Value("${sms.password}")
    private String password;

    /*
     here volatile is used because some other threads might change the token
     when invoking ensureToken() in sendSms()
     in such cases those multiple threads save the changes in its own caches
     which causes inaccurate token in general
     By volatile threads share last changed version of the token in memory
    */

    private volatile String token = null;


    /*
      this method will send a sms message to the phone working async
      because this operation is a blocking I/O operation,
      so it will block main thread of the application causing increasing response time of the request mapping to the sms service API.
      that is why @Async is used in here to separate this operation to another thread
      which causes not to block the main thread,
      helping to decrease response time
    */
    @Async
    public void sendSms(String phoneNumber, String smsCode) {
        String message = String.format(
                "Assalomu aleykum. Konfirensiya boshqaruv tizimi uchun tasdiqlash kodi: %s",
                smsCode
        );

        /*
         here token is null at first, so new token is initiated but with making it sync.
         Otherwise, multi-thread problems(data corruption) might occur when multiple threads working at the same time
        */
        ensureToken();

        try {
            restTemplate.exchange(
                    smsServiceUrl + "/message/sms/send",
                    HttpMethod.POST,
                    getSendSmsBody(phoneNumber, message),
                    Object.class);
        } catch (Exception e) {
            refreshToken();
            sendSms(phoneNumber, smsCode);
        }
    }

    private void ensureToken() {
        if (token == null) {
            synchronized (this) {
                if (token == null) {
                    initToken();
                }
            }
        }
    }

    private void initToken() {
        SmsLoginResponse response = restTemplate.postForEntity(smsServiceUrl + "/auth/login",
                Map.of(
                        "email", email,
                        "password", password
                ),
                SmsLoginResponse.class).getBody();

        if (response.data() == null || response.data().token() == null) {
            throw new RuntimeException("Sms service is not available or returned invalid response");
        }

        this.token = response.data().token();
    }

    @NotNull
    @Contract("_, _ -> new")
    private HttpEntity<?> getSendSmsBody(@NotNull String phoneNumber, String message) {
        Map<String, String> body = Map.of(
                "mobile_phone", phoneNumber.substring(1),
                "message", message,
                "from", "4546"
        );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        return new HttpEntity<>(body, httpHeaders);
    }

    private synchronized void refreshToken() {
        SmsLoginResponse smsLoginResponse = restTemplate.patchForObject(
                smsServiceUrl + "/auth/refresh",
                null,
                SmsLoginResponse.class
        );

        if (smsLoginResponse.data() == null || smsLoginResponse.data().token() == null) {
            throw new RuntimeException("Failed to refresh token: SMS service returned null or invalid response");
        }

        this.token = smsLoginResponse.data().token();
    }

}
