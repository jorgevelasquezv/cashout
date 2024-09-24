package co.com.jorge.cashout.exceptions.responses;

import java.util.List;

public class ErrorResponse {
    private int code;
    private String title;
    private String message;
    private List<ValidationErrorResponse.Violation> errors;

    public ErrorResponse() {
    }

    public ErrorResponse(int code, String title, String message) {
        this.code = code;
        this.title = title;
        this.message = message;
        this.errors = List.of();
    }

    public ErrorResponse(int code, String title, String message, List<ValidationErrorResponse.Violation> errors) {
        this.code = code;
        this.title = title;
        this.message = message;
        this.errors = errors;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ValidationErrorResponse.Violation> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationErrorResponse.Violation> errors) {
        this.errors = errors;
    }
}
