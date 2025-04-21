package vn.fptu.reasbe.utils.converter.criticalreport;

import jakarta.persistence.Converter;
import vn.fptu.reasbe.model.enums.criticalreport.StatusCriticalReport;
import vn.fptu.reasbe.utils.converter.core.BaseEnumConverter;

@Converter(autoApply = true)
public class StatusCriticalReportConverter extends BaseEnumConverter<StatusCriticalReport> {

    public StatusCriticalReportConverter() {
        super(StatusCriticalReport.class);
    }
}
