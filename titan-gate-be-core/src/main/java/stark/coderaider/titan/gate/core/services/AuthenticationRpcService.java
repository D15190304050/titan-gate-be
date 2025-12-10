package stark.coderaider.titan.gate.core.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import stark.coderaider.titan.gate.api.IAuthenticationRpcService;
import stark.coderaider.titan.gate.api.dtos.requests.RegisterAuthenticationRequest;
import stark.coderaider.titan.gate.api.dtos.responses.UserAuthenticationInfo;
import stark.coderaider.titan.gate.core.dao.UserMapper;
import stark.coderaider.titan.gate.core.domain.entities.mysql.User;
import stark.dataworks.basic.params.OutValue;
import stark.dataworks.boot.autoconfig.web.LogArgumentsAndResponse;
import stark.dataworks.boot.web.ServiceResponse;

import java.util.List;

@Slf4j
@DubboService
@Service
@LogArgumentsAndResponse
public class AuthenticationRpcService implements IAuthenticationRpcService
{
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ServiceResponse<Long> registerAuthentication(RegisterAuthenticationRequest request)
    {
        // TODO: Introduce distributed transaction.

        // 1. Validate registration information.
        OutValue<String> message = new OutValue<>();
        if (!validateRegistrationInfo(request, message))
        {
            log.error("Validation error: {}", message.getValue());
            return ServiceResponse.buildErrorResponse(-100, message.getValue());
        }

        // 2. Put the registration info (account info) into DB.
        User user = transfer2User(request);
        userMapper.insert(user);

        // 3. Return success.
        return ServiceResponse.buildSuccessResponse(user.getId());
    }

    private boolean validateRegistrationInfo(RegisterAuthenticationRequest request, OutValue<String> message)
    {
        // Most validations are executed in the titan-treasure service.

        // Validations:
        // 1. If the provided username, phone number, email exists.

        // region 1.
        String username = request.getUsername();
        String phoneNumber = request.getPhoneNumber();
        String phoneNumberCountryCode = request.getPhoneNumberCountryCode();
        String email = request.getEmail();
        List<User> users = userMapper.getUsersByUsernamePhoneNumberEmail(username,
                phoneNumber,
                phoneNumberCountryCode,
                email);
        if (!CollectionUtils.isEmpty(users))
        {
            for (User user : users)
            {
                if (user.getUsername().equals(username))
                {
                    message.setValue("The provided username already exists, try another one.");
                    return false;
                }

                if (user.getPhoneNumber().equals(phoneNumber) && user.getPhoneNumberCountryCode().equals(phoneNumberCountryCode))
                {
                    message.setValue("The provided phone number already exists, try another one.");
                    return false;
                }

                if (user.getEmail().equals(email))
                {
                    message.setValue("The provided email already exists, try another one.");
                    return false;
                }
            }
        }
        // endregion 1.4

        return true;
    }

    private User transfer2User(RegisterAuthenticationRequest request)
    {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User();
        BeanUtils.copyProperties(request, user);
        user.setEncryptedPassword(encodedPassword);
        user.setState(0);
        return user;
    }

    public ServiceResponse<UserAuthenticationInfo> getUserAuthenticationInfo(long userId)
    {
        User user = userMapper.getUserById(userId);
        if (user == null)
            return ServiceResponse.buildErrorResponse(-100, "There is no user with ID: " + userId + ".");

        UserAuthenticationInfo userAuthenticationInfo = new UserAuthenticationInfo();
        BeanUtils.copyProperties(user, userAuthenticationInfo);
        return ServiceResponse.buildSuccessResponse(userAuthenticationInfo);
    }
}