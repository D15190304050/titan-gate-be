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
import stark.coderaider.titan.gate.loginstate.UserPrincipal;
import stark.coderaider.titan.gate.core.domain.dtos.UserInfo;
import stark.coderaider.titan.treasure.api.IUserProfileRpcService;
import stark.coderaider.titan.treasure.api.dtos.responses.UserProfileInfo;
import stark.dataworks.boot.web.ServiceResponse;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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

    public String createToken(UserPrincipal userPrincipal)
    {
        ServiceResponse<UserProfileInfo> userProfileInfo = userProfileRpcService.getUserProfileInfo(userPrincipal.getId());
        if (!userProfileInfo.isSuccess())
            throw new IllegalArgumentException("Failed to get user profile info. " + userProfileInfo.getMessage());

        JWTCreator.Builder builder = JWT.create();

        UserProfileInfo userProfile = userProfileInfo.getData();
        builder.withClaim(SecurityConstants.NICKNAME, userProfile.getNickname());

        builder.withClaim(SecurityConstants.USER_ID, userPrincipal.getId());
        builder.withClaim(SecurityConstants.USERNAME, userPrincipal.getUsername());

        ZonedDateTime expirationTime = ZonedDateTime.now(ZoneOffset.UTC).plusDays(TOKEN_EXPIRATION_IN_DAYS);

        return builder.withExpiresAt(Date.from(expirationTime.toInstant())).sign(jwtAlgorithm);
    }

    public DecodedJWT verify(String token)
    {
        return verifier.verify(token);
    }

    public UserInfo parseUserInfo(String token)
    {
        DecodedJWT decodedJwt = verify(token);
        Long userId = decodedJwt.getClaim(SecurityConstants.USER_ID).asLong();
        String username = decodedJwt.getClaim(SecurityConstants.USERNAME).asString();
        String nickname = decodedJwt.getClaim(SecurityConstants.NICKNAME).asString();

        if (userId != null && username != null)
            return new UserInfo(userId, username, nickname);
        return null;
    }

    @PostConstruct
    public void initialize()
    {
        jwtAlgorithm = Algorithm.HMAC256(secret);
        verifier = JWT.require(jwtAlgorithm).build();
    }
}
