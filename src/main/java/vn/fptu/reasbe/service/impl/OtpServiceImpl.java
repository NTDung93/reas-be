package vn.fptu.reasbe.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.fptu.reasbe.service.EmailService;
import vn.fptu.reasbe.service.OtpService;
import java.security.SecureRandom;

import static vn.fptu.reasbe.model.constant.AppConstants.OTP_LENGTH;

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

    private void sendOtpMail(String fullName, String email, String otp){
        String subject = "Your OTP for Account Verification";
        String content = "Dear " + fullName + ",<br><br>Your OTP for verification is <b>" + otp + "</b>.<br><br>Regards,<br>REAS Team";
        emailService.sendEmail(email, subject, content);
    }

    private String generateOtp() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(secureRandom.nextInt(10));
        }
        return otp.toString();
    }
}
