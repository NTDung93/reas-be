package vn.fptu.reasbe.model.enums.product;

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
public enum ProductStatus {
    IN_STOCK("INST"),
    OUT_OF_STOCK("OOST"),
    COMING_SOON("COMS"),
    NO_LONGER_FOR_SALE("NLFS");

    private final String code;

    public static ProductStatus toEnumConstant(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }

        return Arrays.stream(ProductStatus.values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("ProductStatus", "code", code));
    }
}
