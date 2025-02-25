package vn.fptu.reasbe.model.constant;

import java.util.concurrent.TimeUnit;

public class AppConstants {
    // pagination
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_DIRECTION = "asc";
    public static final String DEFAULT_SORT_BY= "id";

    // regex
    public static final String EMAIL_REGEX = "^[^\\.][a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    public static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$";
    public static final String PHONE_REGEX = "(84|0[35789])([0-9]{8})\\b";
    public static final String GENDER_REGEX = "^(Male|Female|Other)$";

    // auth
    public static final String AUTH_ATTR_NAME = "Authorization";
    public static final String AUTH_VALUE_PREFIX = "Bearer ";
    public static final String SEC_REQ_NAME = "Bear Authentication";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_STAFF = "ROLE_STAFF";

    //otp
    public static final int OTP_LENGTH = 6;

    //item
    public static final int EXPIRED_TIME_WEEKS = 2;
}
