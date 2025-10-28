package stark.coderaider.titan.gate.api;

import stark.coderaider.titan.gate.api.dtos.requests.RegisterAuthenticationRequest;
import stark.coderaider.titan.gate.api.dtos.responses.UserAuthenticationInfo;
import stark.dataworks.boot.dubbo.ValidateArgs;
import stark.dataworks.boot.web.ServiceResponse;

@ValidateArgs
public interface IAuthenticationRpcService
{
    ServiceResponse<Long> registerAuthentication(RegisterAuthenticationRequest request);
    ServiceResponse<UserAuthenticationInfo> getUserAuthenticationInfo(long userId);
}
