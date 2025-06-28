package gym.backend.dto;

public class ResendVerificationRequestDTO {
    private String email;

    public ResendVerificationRequestDTO() {
    }

    public ResendVerificationRequestDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}