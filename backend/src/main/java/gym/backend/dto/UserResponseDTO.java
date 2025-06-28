package gym.backend.dto;

import gym.backend.model.Role;
import java.util.Set;

public class UserResponseDTO {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private Boolean isActive;
    private String avatar;
    private String phone;
    private Set<String> specializations;
    private Set<String> certifications;
    private Integer experience;
    private String createdAt;
    private String lastLogin;

    public UserResponseDTO() {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public Set<String> getCertifications() {
        return certifications;
    }

    public void setCertifications(Set<String> certifications) {
        this.certifications = certifications;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }
}