package mx.unam.fanaticosfc.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {
    private static final String SECRET = "diplomadoJava";
    private static final Long EXPIRATION = 86400000L; // 1 día

    public String generateToken(String username, String rol){
        return Jwts.builder()
                .setSubject(username)
                .claim("rol",rol) //Agrega roles al token
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256,SECRET)
                .compact();
    }

    public String extractUsername(String token){
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extractRol(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("rol", String.class); // Obtiene los roles como una lista
    }


    public Boolean validToken(String token){
        try {
            Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token);
            return true;
        }catch (JwtException e){
            return false;
        }
    }
}
