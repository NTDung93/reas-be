package vn.fptu.reasbe.model.enums.item;

import java.util.Arrays;

import vn.fptu.reasbe.model.exception.ResourceNotFoundException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author ntig
 */
@Getter
@RequiredArgsConstructor
public enum ItemStatus {
    AVAILABLE("AVAI"),
    SOLD_OUT("SOLD"),
    EXPIRED("EXPI"),
    NO_LONGER_FOR_SALE("NLFS");

    private final String code;

    public static ItemStatus toEnumConstant(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }

        return Arrays.stream(ItemStatus.values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("ItemStatus", "code", code));
    }
}
