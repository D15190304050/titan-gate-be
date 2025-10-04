package stark.coderaider.titan.gate.core.services;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stark.coderaider.titan.gate.api.IAuthenticationService;
import stark.coderaider.titan.gate.api.dtos.requests.RegisterAuthenticationRequest;
import stark.coderaider.titan.gate.core.dao.UserMapper;
import stark.dataworks.boot.web.ServiceResponse;

@DubboService
@Service
public class AuthenticationService implements IAuthenticationService
{
    @Autowired
    private UserMapper userMapper;

    @Override
    public ServiceResponse<Boolean> registerAuthentication(RegisterAuthenticationRequest request)
    {
        // TODO: Introduce distributed transaction.



        return null;
    }
}