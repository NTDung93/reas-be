package vn.fptu.reasbe.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ReasApiException extends RuntimeException{
    @Getter
    private final HttpStatus status;

    @Getter
    private final String message;

    public ReasApiException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
