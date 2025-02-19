package vn.fptu.reasbe.model.enums.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.enums.core.BaseEnum;

/**
 *
 * @author ntig
 */
@Getter
@RequiredArgsConstructor
public enum TypeItem implements BaseEnum.CodeAccessible {
    KITCHEN_APPLIANCES("KITC"),
    CLEANING_LAUNDRY_APPLIANCES("CLLA"),
    COOLING_HEATING_APPLIANCES("COHE"),
    ELECTRONICS_ENTERTAINMENT_DEVICES("ELEC"),
    LIGHTING_SECURITY_DEVICES("LISE"),
    BEDRROM_APPLIANCES("BEDR"),
    LIVING_ROOM_APPLIANCES("LIVI"),
    BATHROOM_APPLIANCES("BATH");

    private final String code;

    public static TypeItem toEnumConstant(String code) {
        return BaseEnum.toEnumConstant(TypeItem.class, code);
    }
}
