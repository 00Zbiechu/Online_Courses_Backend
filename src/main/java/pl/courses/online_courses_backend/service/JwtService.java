package pl.courses.online_courses_backend.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.courses.online_courses_backend.entity.UserEntity;

import java.util.Map;
import java.util.function.Function;

@Service
public interface JwtService {

    String extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateToken(UserEntity userEntity);

    String generateToken(Map<String, Object> extraClaims, UserEntity userEntity);

    String generateRefreshToken(UserEntity userEntity);

    boolean isTokenValid(String token, UserDetails userDetails);
}
