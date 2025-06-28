package gym.backend.controller;

import gym.backend.dto.*;
import gym.backend.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;

@RestController
@RequestMapping("/api/auth") // Zmieniono ścieżkę żeby pasowała do frontu
@CrossOrigin(origins = {"http://localhost:4200", "https://gymbucket.com"}) // POPRAWKA #5 - CORS
public class UserController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
        logger.info("UserController initialized");
    }

    // EXISTING ENDPOINTS - IMPROVED

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDTO registrationDTO) {
        try {
            logger.info("Attempting to register user: {}", registrationDTO.getEmail());
            RegisterResponseDTO response = userService.registerUser(registrationDTO);
            logger.info("User registration successful: {}", registrationDTO.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDTO(false, e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDTO loginDTO) {
        try {
            logger.info("Login attempt for user: {}", loginDTO.getEmail());
            LoginResponseDTO response = userService.validateLoginData(loginDTO);
            logger.info("Login successful for user: {}", loginDTO.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Login failed for user {}: {}", loginDTO.getEmail(), e.getMessage());
            return ResponseEntity.badRequest().body(new LoginResponseDTO(false, e.getMessage(), null, null, null, 0));
        }
    }

    // NEW ENDPOINTS - FOR FRONTEND FEATURES

    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmailExists(@RequestParam String email) {
        try {
            logger.debug("Checking email availability: {}", email);
            boolean exists = userService.checkEmailExists(email);
            return ResponseEntity.ok(new EmailCheckResponseDTO(exists));
        } catch (Exception e) {
            logger.error("Error checking email: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDTO(false, "Błąd podczas sprawdzania email"));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDTO request) {
        try {
            logger.debug("Refreshing token");
            LoginResponseDTO response = userService.refreshToken(request.getRefreshToken());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Token refresh failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new LoginResponseDTO(false, e.getMessage(), null, null, null, 0));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequestDTO request) {
        try {
            logger.info("Password reset requested for email: {}", request.getEmail());
            ApiResponseDTO response = userService.forgotPassword(request.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Forgot password failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDTO(false, e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequestDTO request) {
        try {
            logger.info("Password reset attempt with token");
            ApiResponseDTO response = userService.resetPassword(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Password reset failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDTO(false, e.getMessage()));
        }
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody EmailVerificationRequestDTO request) {
        try {
            logger.info("Email verification attempt");
            ApiResponseDTO response = userService.verifyEmail(request.getToken());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Email verification failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDTO(false, e.getMessage()));
        }
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@RequestBody ResendVerificationRequestDTO request) {
        try {
            logger.info("Resending verification email to: {}", request.getEmail());
            ApiResponseDTO response = userService.resendVerification(request.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Resend verification failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDTO(false, e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // For stateless JWT, logout is handled on frontend
        // In the future, you could implement token blacklisting here
        logger.info("User logout");
        return ResponseEntity.ok(new ApiResponseDTO(true, "Wylogowano pomyślnie"));
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(new ApiResponseDTO(true, "Auth service is running"));
    }
}