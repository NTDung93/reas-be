package vn.fptu.reasbe.utils.common;

import java.time.Instant;

import org.springframework.http.HttpStatus;

import vn.fptu.reasbe.model.exception.ReasApiException;

/**
 *
 * @author dungnguyen
 */
public class PaymentCodeHelper {
    public static String generateOrderCode(long itemId) {
        long timestamp = Instant.now().getEpochSecond();
        return timestamp + itemId + "";
    }

    public static long getItemIdFromOrderCode(long orderCode) {
        String orderCodeString = String.valueOf(orderCode);
        int timestampLength = 10;

        if (orderCodeString.length() > timestampLength) {
            String itemIdString = orderCodeString.substring(timestampLength);
            return Long.parseLong(itemIdString);
        } else {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "err.invalidOrderCode: " + orderCode);
        }
    }
}
