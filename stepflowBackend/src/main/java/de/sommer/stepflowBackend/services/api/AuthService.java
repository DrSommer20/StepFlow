package de.sommer.stepflowBackend.services.api;

import de.sommer.stepflowBackend.models.User;

public interface AuthService {

    /**
     * Extracts the email from the token
     * 
     * @param token the token
     * @return the email
     */
    public String extractEmail(String token);

    /**
     * Extracts the Username from the token
     * 
     * @param token the token
     * @return the username
     * @deprecated Use {@link #extractEmail(String)} instead
     */
    public String extractUsername(String token);

    /**
     * Checks if the token is expired
     * 
     * @param token the token
     * @return true if the token is expired, false otherwise
     */
    public boolean isTokenExpired(String token);

    /**
     * Generates a verification token
     * 
     * @param user the user
     * @return the token
     */
    public String generateVerificationToken(User user);

    /**
     * Generates a token
     * 
     * @param userDetails the user
     * @return the token
     */
    public String generateToken(User userDetails);

}