package vn.fptu.reasbe.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.fptu.reasbe.service.EmailService;
import vn.fptu.reasbe.service.OtpService;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@Transactional
public class OtpServiceImpl implements OtpService {
    private final SecureRandom secureRandom = new SecureRandom();
    private final EmailService emailService;

    @Override
    public String generateAndSendOtp(String email, String fullName) {
        String otp = generateOtp();
        sendOtpMail(fullName, email, otp);
        return otp;
    }

    private void sendOtpMail(String fullName, String email, String otp) {
        String subject = "Your OTP for Account Verification";
        String content = "Dear " + fullName + ",<br><br>Your OTP for verification is <b>" + otp + "</b>.<br><br>Regards,<br>REAS Team";
        emailService.sendEmail(email, subject, content);
    }

    private String generateOtp() {
        int otp = 100000 + secureRandom.nextInt(900000); // Random số từ 100000 đến 999999
        return String.valueOf(otp);
    }
}