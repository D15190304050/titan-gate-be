package stark.coderaider.titan.gate.core.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import stark.coderaider.titan.gate.core.constants.SecurityConstants;
import stark.coderaider.titan.gate.core.domain.dtos.UserDetailsImpl;
import stark.coderaider.titan.gate.core.domain.entities.mysql.User;
import stark.coderaider.titan.gate.loginstate.UserInfo;
import stark.coderaider.titan.treasure.api.IUserProfileRpcService;
import stark.coderaider.titan.treasure.api.dtos.responses.UserProfileInfo;
import stark.dataworks.boot.web.ServiceResponse;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;

@Component
public class JwtService
{
    public static final int TOKEN_EXPIRATION_IN_DAYS = 30;

    private JWTVerifier verifier;
    private Algorithm jwtAlgorithm;

    @Value("${titan.gate.jwt-secret}")
    public String secret;

    @DubboReference(url = "${dubbo.service.profile.url}", check = false)
    private IUserProfileRpcService userProfileRpcService;

    public String generateToken(UserDetailsImpl user)
    {
        UserProfileInfo userProfile = getUserProfileInfo(user.getId());
        return generateToken(userProfile.getNickname(), user.getId(), user.getUsername());
    }

    public String generateToken(User user)
    {
        UserProfileInfo userProfile = getUserProfileInfo(user.getId());
        return generateToken(userProfile.getNickname(), user.getId(), user.getUsername());
    }

    private UserProfileInfo getUserProfileInfo(long userId)
    {
        ServiceResponse<UserProfileInfo> userProfileInfo = userProfileRpcService.getUserProfileInfo(userId);
        if (!userProfileInfo.isSuccess())
            throw new IllegalArgumentException("Failed to get user profile info. " + userProfileInfo.getMessage());

        return userProfileInfo.getData();
    }

    private String generateToken(String nickname, Long userId, String username)
    {
        JWTCreator.Builder builder = JWT.create();
        builder.withClaim(SecurityConstants.NICKNAME, nickname);
        builder.withClaim(SecurityConstants.USER_ID, userId);
        builder.withClaim(SecurityConstants.USERNAME, username);

        ZonedDateTime expirationTime = ZonedDateTime.now(ZoneOffset.UTC).plusDays(TOKEN_EXPIRATION_IN_DAYS);

        return builder.withExpiresAt(Date.from(expirationTime.toInstant())).sign(jwtAlgorithm);
    }

    public DecodedJWT verify(String token)
    {
        try
        {
            return verifier.verify(token);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public UserInfo parseUserInfo(String token)
    {
        DecodedJWT decodedJwt = verify(token);
        if (decodedJwt == null)
            return null;

        Long userId = decodedJwt.getClaim(SecurityConstants.USER_ID).asLong();
        String username = decodedJwt.getClaim(SecurityConstants.USERNAME).asString();
        String nickname = decodedJwt.getClaim(SecurityConstants.NICKNAME).asString();

        if (userId != null && username != null)
            return new UserInfo(userId, username, nickname, new ArrayList<>());
        return null;
    }

    @PostConstruct
    public void initialize()
    {
        jwtAlgorithm = Algorithm.HMAC256(secret);
        verifier = JWT.require(jwtAlgorithm).build();
    }
}
