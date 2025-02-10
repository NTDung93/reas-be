package vn.fptu.reasbe.service;

import vn.fptu.reasbe.model.dto.auth.SignupDto;
import vn.fptu.reasbe.model.dto.otp.OtpVerificationRequest;

public interface OtpService {
    SignupDto verifyOtp(OtpVerificationRequest request);
    Boolean generateAndStoreOtp(SignupDto request);
    void resendOtp(String email);
}
