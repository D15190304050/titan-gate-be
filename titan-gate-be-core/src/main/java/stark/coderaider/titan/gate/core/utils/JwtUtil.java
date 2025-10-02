package stark.coderaider.titan.gate.core.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil
{

    @Value("${jwt.secret:mySecretKey}")
    private String secret;

    @Value("${jwt.expiration:86400}")
    private Long expiration;

    public String generateToken(String username)
    {
        Date expireDate = new Date(System.currentTimeMillis() + expiration * 1000);
        return JWT.create()
            .withSubject(username)
            .withExpiresAt(expireDate)
            .sign(Algorithm.HMAC512(secret));
    }

    public String getUsernameFromToken(String token)
    {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getSubject();
    }

    public boolean validateToken(String token)
    {
        try
        {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(secret))
                .build();
            verifier.verify(token);
            return true;
        }
        catch (JWTVerificationException e)
        {
            return false;
        }
    }

    public boolean isTokenExpired(String token)
    {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt().before(new Date());
    }
}