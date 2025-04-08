package vn.fptu.reasbe.utils.common;

import java.time.Instant;

import org.springframework.http.HttpStatus;

import com.mysema.commons.lang.Pair;

import vn.fptu.reasbe.model.exception.ReasApiException;

/**
 *
 * @author dungnguyen
 */
public class PaymentCodeHelper {
    public static String generateOrderCode(Integer planId, Integer itemId) {
        String plandIdString = String.valueOf(planId);
        String timestamp = String.valueOf(Instant.now().getEpochSecond());
        if (planId != null && itemId != null) {
            String itemIdString = String.valueOf(itemId);
            return timestamp + plandIdString + itemIdString;
        } else if (planId != null) {
            return timestamp + plandIdString;
        } else {
            return timestamp;
        }
    }

    public static Pair<Integer, Integer> getItemIdFromOrderCode(long orderCode) {
        int timestampLength = 10;
        String orderCodeString = String.valueOf(orderCode);

        if (orderCodeString.length() > timestampLength) {
            String theRest = orderCodeString.substring(timestampLength);
            if (theRest.length() > 1){
                return Pair.of(Integer.parseInt(theRest.substring(0,1)), Integer.parseInt(theRest.substring(1)));
            }
            return Pair.of(Integer.parseInt(theRest), null);
        } else {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "err.invalidOrderCode: " + orderCode);
        }
    }
}
