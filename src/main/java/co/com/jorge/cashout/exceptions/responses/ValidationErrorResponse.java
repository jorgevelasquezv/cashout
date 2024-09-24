package co.com.jorge.cashout.exceptions.responses;

import java.util.List;

public class ValidationErrorResponse {
    private List<Violation> errors;

    public ValidationErrorResponse() {
    }

    public ValidationErrorResponse(List<Violation> violations) {
        this.errors = violations;
    }

    public List<Violation> getErrors() {
        return errors;
    }

    public void setErrors(List<Violation> violations) {
        this.errors = violations;
    }

    public static class Violation {
        private String code;
        private String message;

        public Violation(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
