package gym.backend.model;

public enum Role {
    TRAINER("trainer"),
    ADMIN("admin"),
    CLIENT("client");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}