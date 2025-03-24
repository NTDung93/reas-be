package vn.fptu.reasbe.service;

import vn.fptu.reasbe.model.dto.payos.CreatePaymentLinkRequest;
import vn.fptu.reasbe.model.dto.payos.WebhookUrlDto;
import vn.payos.type.CheckoutResponseData;

public interface PayOSService {
    CheckoutResponseData createPayment(CreatePaymentLinkRequest createPaymentLinkRequest);
    String confirmWebhook(WebhookUrlDto webhookUrlDto);
}
