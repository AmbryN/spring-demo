package edu.ambryn.demo.security;

import edu.ambryn.demo.models.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JwtUtils {

    public String generateJwt(MyUserDetails userDetails) {
        Map<String, Object> data = new HashMap<>();
        data.put("firstname", userDetails.getUser().getFirstname());
        data.put("lastname", userDetails.getUser().getLastname());
        data.put("roles", userDetails.getUser().getRoles().stream().map(Role::getName).collect(Collectors.joining(",")));

        return Jwts.builder()
                .setClaims(data)
                .setSubject(userDetails.getUsername())
                .signWith(SignatureAlgorithm.HS256, "my_little_pony")
                .compact();
    }

    public Optional<String> extractToken(String jwt) {
        return jwt.startsWith("Bearer ") ? Optional.of(jwt.substring(7)) : Optional.empty();
    }

    public Optional<Claims> getData(String jwt) {
        Optional<Claims> claims = Optional.empty();
        try {
            claims = Optional.of(
                    Jwts.parser()
                            .setSigningKey("my_little_pony")
                            .parseClaimsJws(jwt)
                            .getBody()
            );
        } catch (SignatureException se) {
            System.out.println("Signature is invalid");
        }
        return claims;
    }
}
