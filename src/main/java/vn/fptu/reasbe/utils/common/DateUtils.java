package vn.fptu.reasbe.utils.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

import org.apache.commons.lang3.Validate;

/**
 *
 * @author ntig
 */
public abstract class DateUtils {
    private static LocalDate currentDate = null;
    private static LocalDateTime currentDateTime = null;
//    static {
//        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
//        yaml.setResources(new ClassPathResource("application-common-util.yml"));
//        String val = yaml.getObject().getProperty("common-util.currentDate");
//        if (StringUtils.isNotEmpty(val)) {
//            currentDate = LocalDate.parse(val, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//            currentDateTime = LocalDateTime.of(currentDate, LocalTime.MIN);
//        }
//    }

    private DateUtils() {
    }


    public static LocalDateTime getCurrentDateTime() {
        // Return simulated value if having, otherwise return system date
        return currentDateTime != null ? currentDateTime : LocalDateTime.now();
    }

    public static LocalDate getCurrentDate() {
        // Return simulated value if having, otherwise return system date
        return currentDate != null ? currentDate : LocalDate.now();
    }

    public static void setCurrentDate(LocalDate value) {
        currentDate = value;
    }

    public static Integer getCurrentYear() {
        return getCurrentDate().getYear();
    }

    public static Integer getCurrentMonth() { return getCurrentDate().getMonthValue(); }

    public static boolean isTwoPeriodOverlap(LocalDate startDate1, LocalDate endDate1, LocalDate startDate2, LocalDate endDate2) {
        Validate.notNull(startDate1, "Start date of the first period cannot be null");
        Validate.notNull(startDate2, "Start date of the second period cannot be null");

        // If both periods start on the same date, or neither periods have end dates, there is an overlap.
        if (startDate1.isEqual(startDate2) || (endDate1 == null && endDate2 == null)) {
            return true;
        }

        // If the earliest period does not have an end date or the most recent period starts before the earliest period ends, there is an overlap.
        if (startDate1.isBefore(startDate2)) {
            // Here the period defined by startDate1 and endDate1 is the earliest period.
            return endDate1 == null || DateUtils.isBeforeOrEquals(startDate2, endDate1);
        } else { // startDate1 > startDate2
            // Here the period defined by startDate2 and endDate2 is the earliest period.
            return endDate2 == null || DateUtils.isBeforeOrEquals(startDate1, endDate2);
        }
    }

    public static boolean isBeforeOrEquals(LocalDate dateFirst, LocalDate dateSecond) {
        if (dateFirst == null || dateSecond == null) {
            return false;
        }

        return (dateFirst.isBefore(dateSecond) || dateFirst.equals(dateSecond));
    }

    public static LocalDateTime getStartOfCurrentDay() {
        return getCurrentDate().atStartOfDay();
    }

    public static LocalDateTime getFirstDayOfCurrentMonth() {
        return LocalDateTime.of(getCurrentYear(), Month.of(getCurrentMonth()), 1, 0, 0, 0);
    }

    public static LocalDateTime getLastDayOfCurrentMonth() {
       return getFirstDayOfCurrentMonth().with(TemporalAdjusters.lastDayOfMonth());
    }
}
