package vn.fptu.reasbe.service;

import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.fptu.reasbe.model.entity.PaymentHistory;

/**
 *
 * @author dungnguyen
 */
public interface PaymentHistoryService {
    Boolean payOsTransferHandler(ObjectNode body) throws Exception;
    PaymentHistory getPaymentHistoryById(Integer id);
}
