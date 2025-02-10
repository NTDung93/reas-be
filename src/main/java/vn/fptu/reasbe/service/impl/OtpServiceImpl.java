package vn.fptu.reasbe.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.fptu.reasbe.model.dto.auth.SignupDto;
import vn.fptu.reasbe.model.dto.otp.OtpVerificationRequest;
import vn.fptu.reasbe.model.exception.ReasApiException;
import vn.fptu.reasbe.service.EmailService;
import vn.fptu.reasbe.service.OtpService;

import java.security.SecureRandom;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static vn.fptu.reasbe.model.constant.AppConstants.OTP_EXPIRATION_MILLIS;
import static vn.fptu.reasbe.model.constant.AppConstants.OTP_LENGTH;

@Service
@Transactional
public class OtpServiceImpl implements OtpService {

    private final SecureRandom secureRandom = new SecureRandom();
    private final ConcurrentMapCache otpCache;
    private final EmailService emailService;
    private String timestampStr = "timestamp";

    public OtpServiceImpl(ConcurrentMapCacheManager cacheManager, EmailService emailService) {
        this.otpCache = (ConcurrentMapCache) cacheManager.getCache("otpCache");
        this.emailService = emailService;
    }

    @Override
    public SignupDto verifyOtp(OtpVerificationRequest request) {
        Map<String, Object> registrationData = getRegistrationData(request.getEmail());

        String cachedOtp = (String) registrationData.get("otp");
        long timestamp = (long) registrationData.get(timestampStr);

        if (System.currentTimeMillis() - timestamp > OTP_EXPIRATION_MILLIS) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "OTP expired. Please request a new one.");
        }

        if (!cachedOtp.equals(request.getOtp())) throw new ReasApiException(HttpStatus.BAD_REQUEST, "Invalid OTP.");

        SignupDto userData = (SignupDto) registrationData.get("user");
        otpCache.evict(request.getEmail());
        return userData;
    }


    @Override
    public Boolean generateAndStoreOtp(SignupDto request) {
        String otp = generateOtp();

        Map<String, Object> registrationData = new ConcurrentHashMap<>();
        registrationData.put("user", request);
        registrationData.put("otp", otp);
        registrationData.put(timestampStr, System.currentTimeMillis());

        otpCache.put(request.getEmail(), registrationData);
        sendOtpMail(request.getFullName(), request.getEmail(), otp);
        return true;
    }

    @Override
    public void resendOtp(String email) {
        Map<String, Object> registrationData = getRegistrationData(email);

        long timestamp = (long) registrationData.get(timestampStr);

        if (System.currentTimeMillis() - timestamp < TimeUnit.MINUTES.toMillis(1)) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "Please wait a minute before requesting a new OTP.");
        }
        String otp = generateOtp();
        registrationData.put("otp", otp);
        registrationData.put(timestampStr, System.currentTimeMillis());
        SignupDto userData = (SignupDto) registrationData.get("user");
        sendOtpMail(userData.getFullName(), userData.getEmail(), otp);
    }

    private void sendOtpMail(String fullName, String email, String otp){
        String subject = "Your OTP for Account Verification";
        String content = "Dear " + fullName + ",<br><br>Your OTP for verification is <b>" + otp + "</b>.<br><br>Regards,<br>REAS Team";
        emailService.sendEmail(email, subject, content);
    }

    private Map<String, Object> getRegistrationData(String email){
        if (otpCache.get(email).get() == null) {
            throw new ReasApiException(HttpStatus.NOT_FOUND, "Registration data not found, please register again!");
        }
        return (Map<String, Object>) otpCache.get(email).get();
    }

    private String generateOtp() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(secureRandom.nextInt(10));
        }
        return otp.toString();
    }

    @Scheduled(fixedRate = 3600000)
    public void evictExpiredOtps() {
        if (otpCache.getNativeCache() instanceof ConcurrentHashMap<?, ?> cacheMap) {
            Iterator<? extends Map.Entry<?, ?>> iterator = cacheMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<?, ?> entry = iterator.next();
                Map<String, Object> registrationData = (Map<String, Object>) entry.getValue();
                long timestamp = (long) registrationData.get(timestampStr);

                if (System.currentTimeMillis() - timestamp > OTP_EXPIRATION_MILLIS) {
                    iterator.remove();
                }
            }
        }
    }

}
