package vn.fptu.reasbe.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.service.PaymentHistoryService;

/**
 *
 * @author dung
 */
@RestController
@RequestMapping("/api/v1/payment-history")
@RequiredArgsConstructor
public class PaymentHistoryController {

    private final PaymentHistoryService paymentHistoryService;

    @GetMapping(value = "/payos-transfer-handler")
    public void payosTransferHandler(@RequestBody ObjectNode body) throws Exception {
        paymentHistoryService.payOsTransferHandler(body);
    }
}
