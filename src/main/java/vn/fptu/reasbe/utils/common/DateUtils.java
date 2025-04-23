package vn.fptu.reasbe.utils.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;

import org.apache.commons.lang3.Validate;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;

/**
 *
 * @author ntig
 */
public abstract class DateUtils {
    private static LocalDate currentDate = null;
    private static LocalDateTime currentDateTime = null;

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

    public static LocalDateTime toStartOfDay(LocalDateTime date) {
        return date.toLocalDate().atStartOfDay();
    }

    public static LocalDateTime getFirstDayOfCurrentMonth() {
        return LocalDateTime.of(getCurrentYear(), Month.of(getCurrentMonth()), 1, 0, 0, 0);
    }

    public static LocalDateTime getLastDayOfCurrentMonth() {
       return getFirstDayOfCurrentMonth().with(TemporalAdjusters.lastDayOfMonth());
    }

    // ────────────────────────────────────────────────────────────────────────────────
    // New methods for QueryDSL date extraction logic
    // ────────────────────────────────────────────────────────────────────────────────

    /**
     * Creates a QueryDSL expression to extract the month from a date expression.
     *
     * @param dateTimeExpression an expression representing a date/time value.
     * @return a NumberExpression representing the month (1-12)
     */
    public static NumberExpression<Integer> extractMonth(Expression<LocalDateTime> dateTimeExpression) {
        return Expressions.numberTemplate(Integer.class, "MONTH({0})", dateTimeExpression);
    }

    /**
     * Creates a QueryDSL expression to extract the year from a date expression.
     *
     * @param dateTimeExpression an expression representing a date/time value.
     * @return a NumberExpression representing the year.
     */
    public static NumberExpression<Integer> extractYear(Expression<LocalDateTime> dateTimeExpression) {
        return Expressions.numberTemplate(Integer.class, "YEAR({0})", dateTimeExpression);
    }

    public static LocalDateTime getStartOfSpecificMonth(Integer month, Integer year){
        if (month == null || year == null) {
            return null;
        }
        return LocalDateTime.of(year, Month.of(month), 1, 0, 0, 0);
    }

    public static LocalDateTime getEndOfSpecificMonth(Integer month, Integer year){
        if (month == null || year == null) {
            return null;
        }
        return LocalDateTime.of(year, Month.of(month), 1, 0, 0, 0).with(TemporalAdjusters.lastDayOfMonth());
    }

    public static LocalDateTime getEndDateByStartDateAndDuration(LocalDateTime startDate, float duration) {
        long fullMonths = (long) duration;
        double fractionalMonth = duration - fullMonths;

        LocalDateTime intermediateDate = startDate.plusMonths(fullMonths);

        YearMonth yearMonth = YearMonth.from(intermediateDate);
        int daysInMonth = yearMonth.lengthOfMonth();

        long extraDays = Math.round(fractionalMonth * daysInMonth);

        return intermediateDate.plusDays(extraDays);
    }
}
