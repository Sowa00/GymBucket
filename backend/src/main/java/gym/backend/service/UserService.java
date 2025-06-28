package gym.backend.service;

import gym.backend.dto.*;
import gym.backend.exception.DuplicateEmailException;
import gym.backend.model.User;
import gym.backend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import org.slf4j.Logger;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired // POPRAWKA #1 - dodałem brakującą adnotację
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecretKey jwtSecretKey; // POPRAWKA #4 - używamy stałego klucza z konfiguracji

    @Value("${jwt.expirationMs}")
    private long jwtExpirationMs;

    @Value("${jwt.refreshExpirationMs}")
    private long jwtRefreshExpirationMs;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // EXISTING METHODS - IMPROVED

    public RegisterResponseDTO registerUser(UserRegistrationDTO registrationDTO) {
        validateRegistrationData(registrationDTO);

        // Validate password confirmation
        if (!registrationDTO.isPasswordMatching()) {
            throw new IllegalArgumentException("Hasła muszą być identyczne");
        }

        String encodedPassword = passwordEncoder.encode(registrationDTO.getPassword());

        User user = new User(registrationDTO.getEmail(), encodedPassword,
                registrationDTO.getFirstName(), registrationDTO.getLastName());

        // Set additional fields
        user.setPhone(registrationDTO.getPhone());
        user.setSpecializations(registrationDTO.getSpecializations());
        user.setIsEmailVerified(false); // Require email verification
        user.setEmailVerificationToken(generateVerificationToken());

        User savedUser = userRepository.save(user);

        // TODO: Send verification email
        logger.info("User registered successfully: {}", savedUser.getEmail());

        UserResponseDTO userResponse = convertToUserResponseDTO(savedUser);

        return new RegisterResponseDTO(
                true,
                "Konto zostało utworzone pomyślnie. Sprawdź swoją skrzynkę email w celu weryfikacji.",
                userResponse,
                true // requires verification
        );
    }

    boolean validateRegistrationData(UserRegistrationDTO registrationDTO) {
        if (!isValidEmail(registrationDTO.getEmail())) {
            throw new IllegalArgumentException("Nieprawidłowy format email");
        }

        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new DuplicateEmailException("Email już istnieje w systemie");
        }

        // Validate required fields
        if (registrationDTO.getFirstName() == null || registrationDTO.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("Imię jest wymagane");
        }

        if (registrationDTO.getLastName() == null || registrationDTO.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nazwisko jest wymagane");
        }

        if (registrationDTO.getAcceptTerms() == null || !registrationDTO.getAcceptTerms()) {
            throw new IllegalArgumentException("Musisz zaakceptować regulamin");
        }

        return true;
    }

    boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.length() > 5;
    }

    public LoginResponseDTO validateLoginData(UserLoginDTO loginDTO) {
        if (loginDTO.getEmail() == null || loginDTO.getPassword() == null) {
            throw new IllegalArgumentException("Email i hasło są wymagane");
        }

        User user = getUserByEmail(loginDTO.getEmail());

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Nieprawidłowy email lub hasło");
        }

        if (!user.getIsActive()) {
            throw new IllegalArgumentException("Konto zostało zablokowane");
        }

        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Generate tokens
        String token = generateJwtToken(user.getEmail());
        String refreshToken = generateRefreshToken(user.getEmail());

        UserResponseDTO userResponse = convertToUserResponseDTO(user);

        return new LoginResponseDTO(
                true,
                "Logowanie pomyślne",
                userResponse,
                token,
                refreshToken,
                jwtExpirationMs / 1000 // convert to seconds
        );
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika z podanym emailem"));
    }

    public String generateJwtToken(String email) {
        User user = getUserByEmail(email);

        String token = Jwts.builder()
                .setSubject(email)
                .claim("userId", user.getId())
                .claim("role", user.getRole().getValue())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(jwtSecretKey, SignatureAlgorithm.HS512) // POPRAWKA #4 - używamy stałego klucza
                .compact();

        logger.debug("Generated JWT Token for user: {}", email);
        return token;
    }

    // NEW METHODS - FOR FRONTEND ENDPOINTS

    public boolean checkEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .claim("type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpirationMs))
                .signWith(jwtSecretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public LoginResponseDTO refreshToken(String refreshToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey)
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            String email = claims.getSubject();
            String tokenType = claims.get("type", String.class);

            if (!"refresh".equals(tokenType)) {
                throw new IllegalArgumentException("Invalid token type");
            }

            User user = getUserByEmail(email);

            if (!user.getIsActive()) {
                throw new IllegalArgumentException("Konto zostało zablokowane");
            }

            String newToken = generateJwtToken(email);
            String newRefreshToken = generateRefreshToken(email);

            UserResponseDTO userResponse = convertToUserResponseDTO(user);

            return new LoginResponseDTO(
                    true,
                    "Token odświeżony pomyślnie",
                    userResponse,
                    newToken,
                    newRefreshToken,
                    jwtExpirationMs / 1000
            );

        } catch (Exception e) {
            throw new IllegalArgumentException("Nieprawidłowy refresh token");
        }
    }

    public ApiResponseDTO forgotPassword(String email) {
        try {
            User user = getUserByEmail(email);

            String resetToken = generatePasswordResetToken();
            user.setResetPasswordToken(resetToken);
            user.setResetPasswordTokenExpiry(LocalDateTime.now().plusHours(1)); // 1 hour expiry

            userRepository.save(user);

            // TODO: Send password reset email
            logger.info("Password reset token generated for user: {}", email);

            return new ApiResponseDTO(true, "Link do resetowania hasła został wysłany na Twój email");

        } catch (IllegalArgumentException e) {
            // Don't reveal if email exists or not for security
            return new ApiResponseDTO(true, "Jeśli email istnieje w systemie, otrzymasz link do resetowania hasła");
        }
    }

    public ApiResponseDTO resetPassword(ResetPasswordRequestDTO request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Hasła muszą być identyczne");
        }

        User user = userRepository.findByResetPasswordToken(request.getToken())
                .orElseThrow(() -> new IllegalArgumentException("Nieprawidłowy lub wygasły token"));

        if (user.getResetPasswordTokenExpiry() == null ||
                user.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token wygasł");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiry(null);

        userRepository.save(user);

        logger.info("Password reset successfully for user: {}", user.getEmail());

        return new ApiResponseDTO(true, "Hasło zostało zmienione pomyślnie");
    }

    public ApiResponseDTO verifyEmail(String token) {
        User user = userRepository.findByEmailVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Nieprawidłowy token weryfikacyjny"));

        user.setIsEmailVerified(true);
        user.setEmailVerificationToken(null);

        userRepository.save(user);

        logger.info("Email verified successfully for user: {}", user.getEmail());

        return new ApiResponseDTO(true, "Email został zweryfikowany pomyślnie");
    }

    public ApiResponseDTO resendVerification(String email) {
        User user = getUserByEmail(email);

        if (user.getIsEmailVerified()) {
            throw new IllegalArgumentException("Email jest już zweryfikowany");
        }

        user.setEmailVerificationToken(generateVerificationToken());
        userRepository.save(user);

        // TODO: Send verification email
        logger.info("Verification email resent for user: {}", email);

        return new ApiResponseDTO(true, "Email weryfikacyjny został wysłany ponownie");
    }

    // HELPER METHODS

    private UserResponseDTO convertToUserResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId().toString());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole());
        dto.setIsActive(user.getIsActive());
        dto.setAvatar(user.getAvatar());
        dto.setPhone(user.getPhone());
        dto.setSpecializations(user.getSpecializations());
        dto.setCertifications(user.getCertifications());
        dto.setExperience(user.getExperience());
        dto.setCreatedAt(user.getCreatedAt() != null ? user.getCreatedAt().toString() : null);
        dto.setLastLogin(user.getLastLogin() != null ? user.getLastLogin().toString() : null);
        return dto;
    }

    private String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }

    private String generatePasswordResetToken() {
        return UUID.randomUUID().toString();
    }

    // Method to validate JWT token
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    // Method to get email from JWT token
    public String getEmailFromJwtToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}