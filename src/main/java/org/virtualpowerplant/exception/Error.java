package org.virtualpowerplant.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Error {
    private List<ErrorDetail> errors;

    private String message;

    private String code;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ErrorDetail {
        private String field;

        private String message;
    }
}
