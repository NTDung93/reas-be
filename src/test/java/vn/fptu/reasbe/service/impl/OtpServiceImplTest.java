package vn.fptu.reasbe.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.http.HttpStatus;
import vn.fptu.reasbe.model.dto.auth.SignupDto;
import vn.fptu.reasbe.model.dto.otp.OtpVerificationRequest;
import vn.fptu.reasbe.model.exception.ReasApiException;
import vn.fptu.reasbe.service.EmailService;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static vn.fptu.reasbe.model.constant.AppConstants.OTP_EXPIRATION_MILLIS;

@ExtendWith(MockitoExtension.class)
class OtpServiceImplTest {

    @InjectMocks
    private OtpServiceImpl otpService;

    @Mock
    private ConcurrentMapCacheManager cacheManager;

    @Mock
    private EmailService emailService;

    private ConcurrentMapCache otpCache;

    private SignupDto signupDto;

    @BeforeEach
    void setUp() {
        otpCache = new ConcurrentMapCache("otpCache");
        when(cacheManager.getCache("otpCache")).thenReturn(otpCache);
        otpService = new OtpServiceImpl(cacheManager, emailService);
        signupDto = new SignupDto( "Test User", "test@example.com", "password123", "Nguyen Van A", "0923123123", "Male");
    }

    @Test
    void testGenerateAndStoreOtp_Success() {
        boolean result = otpService.generateAndStoreOtp(signupDto);

        assertTrue(result);
        assertNotNull(otpCache.get(signupDto.getEmail()));
        verify(emailService, times(1)).sendEmail(eq("test@example.com"), anyString(), contains("Your OTP"));
    }

    @Test
    void testVerifyOtp_Success() {
        otpService.generateAndStoreOtp(signupDto);

        // Extract the OTP from the cache
        Map<String, Object> registrationData = (Map<String, Object>) otpCache.get(signupDto.getEmail()).get();
        String otp = (String) registrationData.get("otp");

        OtpVerificationRequest request = new OtpVerificationRequest("test@example.com", otp);
        SignupDto verifiedUser = otpService.verifyOtp(request);

        assertEquals(signupDto, verifiedUser);
        assertNull(otpCache.get(request.getEmail()));
    }

    @Test
    void testVerifyOtp_InvalidOtp() {
        otpService.generateAndStoreOtp(signupDto);

        OtpVerificationRequest request = new OtpVerificationRequest("test@example.com", "654321");

        ReasApiException exception = assertThrows(ReasApiException.class, () -> otpService.verifyOtp(request));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Invalid OTP.", exception.getMessage());
    }

    @Test
    void testVerifyOtp_ExpiredOtp() throws InterruptedException {
        otpService.generateAndStoreOtp(signupDto);
        Thread.sleep(OTP_EXPIRATION_MILLIS + 10); // Simulate OTP expiration

        OtpVerificationRequest request = new OtpVerificationRequest("test@example.com", "123456");

        ReasApiException exception = assertThrows(ReasApiException.class, () -> otpService.verifyOtp(request));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("OTP expired. Please request a new one.", exception.getMessage());
    }

    @Test
    void testResendOtp_TooSoon() {
        otpService.generateAndStoreOtp(signupDto);

        ReasApiException exception = assertThrows(ReasApiException.class, () -> otpService.resendOtp("test@example.com"));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Please wait a minute before requesting a new OTP.", exception.getMessage());
    }

    @Test
    void testResendOtp_Success() throws InterruptedException {
        otpService.generateAndStoreOtp(signupDto);
        Thread.sleep(TimeUnit.MINUTES.toMillis(1)); // Wait for resend timeout

        otpService.resendOtp("test@example.com");
        verify(emailService, times(2)).sendEmail(eq("test@example.com"), anyString(), contains("Your OTP"));
    }
}
