package vn.fptu.reasbe.service;

public interface OtpService {
    String generateAndSendOtp(String email, String fullName);
}
