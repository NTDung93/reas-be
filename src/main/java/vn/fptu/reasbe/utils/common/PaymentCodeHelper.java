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
        long timestamp = Instant.now().getEpochSecond();
        if (planId != null && itemId != null) {
            return timestamp + planId + "_" + itemId;
        } else if (planId != null) {
            return timestamp + planId + "";
        } else {
            return String.valueOf(timestamp);
        }
    }


    public static Pair<Integer, Integer> getItemIdFromOrderCode(long orderCode) {
        int timestampLength = 10;
        String orderCodeString = String.valueOf(orderCode);

        if (orderCodeString.length() > timestampLength) {
            String theRest = orderCodeString.substring(timestampLength);
            if (theRest.contains("_")){
                return Pair.of(Integer.parseInt(theRest.split("_")[0]), Integer.parseInt(theRest.split("_")[1]));
            }
            return Pair.of(Integer.parseInt(theRest), null);
        } else {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "err.invalidOrderCode: " + orderCode);
        }
    }
}
