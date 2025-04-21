package vn.fptu.reasbe.utils.converter.criticalreport;

import jakarta.persistence.Converter;
import vn.fptu.reasbe.model.enums.criticalreport.TypeCriticalReport;
import vn.fptu.reasbe.utils.converter.core.BaseEnumConverter;

@Converter(autoApply = true)
public class TypeCriticalReportConverter extends BaseEnumConverter<TypeCriticalReport> {

    public TypeCriticalReportConverter() {
        super(TypeCriticalReport.class);
    }
}