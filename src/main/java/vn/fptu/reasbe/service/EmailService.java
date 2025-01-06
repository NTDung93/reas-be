package vn.fptu.reasbe.service;

public interface EmailService {
    void sendEmail(String to, String subject, String content);
    void sendEmailWithQR(String to, String subject, String content, byte[] qrCodeImage);
}
