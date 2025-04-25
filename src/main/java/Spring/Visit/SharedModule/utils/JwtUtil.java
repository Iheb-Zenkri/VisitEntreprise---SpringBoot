package Spring.Visit.SharedModule.utils;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@SuppressWarnings("ALL")
@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(String username,String role,boolean expiresIn30Days) {
        long expirationTime = 1000 * 60 * 60 * 24 ; // Token expires after 1 days
        if(expiresIn30Days) expirationTime*=30;
        String token = Jwts.builder()
                .setSubject(username)
                .claim("role",role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        // Log token generation
        logger.info("Generated JWT token for username: {}", username);
        return token;
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        String username = extractClaims(token).getSubject();

        // Log username extraction from token
        logger.info("Extracted username from token: {}", username);
        return username;
    }

    public boolean isTokenExpired(String token) {
        boolean isExpired = extractClaims(token).getExpiration().before(new Date());

        // Log token expiration status
        if (isExpired) {
            logger.warn("Token is expired: {}", token);
        }
        return isExpired;
    }

    public boolean validateToken(String token, String username) {
        boolean isValid = (username.equals(extractUsername(token)) && !isTokenExpired(token));

        // Log token validation result
        if (isValid) {
            logger.info("Token validation successful for username: {}", username);
        } else {
            logger.warn("Token validation failed for username: {}", username);
        }

        return isValid;
    }
}
