package gym.backend.dto;

public class RegisterResponseDTO {
    private boolean success;
    private String message;
    private UserResponseDTO user;
    private boolean requiresVerification;

    public RegisterResponseDTO() {
    }

    public RegisterResponseDTO(boolean success, String message, UserResponseDTO user, boolean requiresVerification) {
        this.success = success;
        this.message = message;
        this.user = user;
        this.requiresVerification = requiresVerification;
    }

    // Getters and Setters
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

    public UserResponseDTO getUser() {
        return user;
    }

    public void setUser(UserResponseDTO user) {
        this.user = user;
    }

    public boolean isRequiresVerification() {
        return requiresVerification;
    }

    public void setRequiresVerification(boolean requiresVerification) {
        this.requiresVerification = requiresVerification;
    }
}