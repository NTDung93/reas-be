package vn.fptu.reasbe.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.fptu.reasbe.service.EmailService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OtpServiceImplTest {

    @Mock
    private EmailService emailService;
    private OtpServiceImpl otpService;

    private String email;
    private String fullName;

    @BeforeEach
    void setUp() {
        otpService = new OtpServiceImpl(emailService);
        email = "user@example.com";
        fullName = "John Doe";
    }

    @Test
    void generateAndSendOtp_ValidInput_GeneratesOtpAndSendsEmail() {
        // When
        String otp = otpService.generateAndSendOtp(email, fullName);

        // Then: Verify OTP is 6-digit numeric
        assertThat(otp).hasSize(6);
        assertThat(otp).matches("\\d+");

        // Capture email arguments
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);

        verify(emailService).sendEmail(
                emailCaptor.capture(),
                subjectCaptor.capture(),
                contentCaptor.capture()
        );

        // Verify email parameters
        assertThat(emailCaptor.getValue()).isEqualTo(email);
        assertThat(subjectCaptor.getValue()).isEqualTo("Your OTP for Account Verification");
        assertThat(contentCaptor.getValue())
                .contains(fullName)
                .contains(otp);
    }
}
