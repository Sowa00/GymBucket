package gym.backend.dto;

import java.util.Set;

public class UserRegistrationDTO {
    private String email;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String phone;
    private Set<String> specializations;
    private Boolean acceptTerms;
    private Boolean acceptNewsletter;

    public UserRegistrationDTO() {
    }

    public UserRegistrationDTO(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName; // POPRAWKA #3 - dodałem brakującą metodę
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<String> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(Set<String> specializations) {
        this.specializations = specializations;
    }

    public Boolean getAcceptTerms() {
        return acceptTerms;
    }

    public void setAcceptTerms(Boolean acceptTerms) {
        this.acceptTerms = acceptTerms;
    }

    public Boolean getAcceptNewsletter() {
        return acceptNewsletter;
    }

    public void setAcceptNewsletter(Boolean acceptNewsletter) {
        this.acceptNewsletter = acceptNewsletter;
    }

    // Helper method
    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }

    @Override
    public String toString() {
        return "UserRegistrationDTO{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", acceptTerms=" + acceptTerms +
                ", acceptNewsletter=" + acceptNewsletter +
                '}';
    }
}