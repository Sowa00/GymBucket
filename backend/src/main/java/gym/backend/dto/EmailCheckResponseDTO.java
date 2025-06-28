
package gym.backend.dto;

public class EmailCheckResponseDTO {
    private boolean exists;

    public EmailCheckResponseDTO() {
    }

    public EmailCheckResponseDTO(boolean exists) {
        this.exists = exists;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }
}