package _bbu.lawfirmapi.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;
import _bbu.lawfirmapi.models.DTO.appuser.response.AppUserResponse;
import _bbu.lawfirmapi.models.Entity.AppUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtService {

    public static final long JWT_TOKEN_VALIDITY = 100 * 60 * 60; // 3600s = 1hour
//    public static final String SECRET = "8G8pJXchAVMPYQTWVmP2DhzMLFqezz49wSYCNNZedss7Y0dH87V7c6QKXEqA9k5cShn0N9kM0aF5H8xvlL6mE=";
    public static final String SECRET = "6c3f76e731c0322e777cb688cacd25d60b67a2ee30844a1c24dafdd5ce8a1b79";

    private String createToken(Map<String, Object> claim, String subject) {
        return Jwts.builder()
                .claims(claim)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(getSignKey()).compact();
    }
    // 1. generate signature key
    private SecretKey getSignKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 2. generate token for user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        AppUser appUser = (AppUser)  userDetails;
        return createToken(claims, appUser.getUsername());
    }

    // 3. retrieving any information from token we will need the secret key
    private Claims extractAllClaim(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 4. extract a specific claim from the JWT token’s claims.
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaim(token);
        return claimsResolver.apply(claims);
    }
    // 5. retrieve username from jwt token
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 6. retrieve expiration date from jwt token
    public Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 7. check expired token
    private Boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    // 8. validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}