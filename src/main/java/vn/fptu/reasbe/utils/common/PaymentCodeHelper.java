package vn.fptu.reasbe.utils.common;

import java.time.Instant;

import org.apache.commons.lang3.tuple.Triple;
import org.springframework.http.HttpStatus;

import vn.fptu.reasbe.model.exception.ReasApiException;

/**
 *
 * @author dungnguyen
 */
public class PaymentCodeHelper {
    public static String generateOrderCode(Integer planId, Integer userId, Integer itemId) {
        String timestampInMinutes = String.valueOf(Instant.now().getEpochSecond() / 60);

        String planIdStr = planId != null ? planId.toString() : "0";
        String userIdStr = userId != null ? String.format("%03d", userId) : "000";
        String itemIdStr = itemId != null ? String.format("%04d", itemId) : "0000";

        return timestampInMinutes + planIdStr + userIdStr + itemIdStr;
    }

    public static Triple<Integer, Integer, Integer> getInfoFromOrderCode(long orderCode) {
        String orderCodeStr = String.valueOf(orderCode);

        if (orderCodeStr.length() > 16) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "err.invalidOrderCode: " + orderCode);
        }

        int timestampLength = 8;

        int planId = Integer.parseInt(orderCodeStr.substring(timestampLength, timestampLength + 1));
        int userId = Integer.parseInt(orderCodeStr.substring(timestampLength + 1, timestampLength + 4));
        int itemId = Integer.parseInt(orderCodeStr.substring(timestampLength + 4));

        return Triple.of(planId, userId, itemId);
    }
}
