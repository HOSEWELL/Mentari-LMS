package app.rest;

public class ResponseStatus {

    private boolean success = true;
    private String message = "Request processed successfully";

    public ResponseStatus() {
    }

    public ResponseStatus(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}