package org.virtualpowerplant.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BadRequestException extends RuntimeException {

    private HttpStatus status = HttpStatus.BAD_REQUEST;
    private String message = "Bad Request Error";
    private List<Error.ErrorDetail> errors = new ArrayList<>();

    public BadRequestException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public BadRequestException(String message) {
        this.message = message;
    }
}
