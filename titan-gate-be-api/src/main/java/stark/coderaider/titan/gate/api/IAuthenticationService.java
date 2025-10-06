package stark.coderaider.titan.gate.api;

import stark.coderaider.titan.gate.api.dtos.requests.RegisterAuthenticationRequest;
import stark.dataworks.boot.web.ServiceResponse;

@ValidateArgs
public interface IAuthenticationService
{
    ServiceResponse<Long> registerAuthentication(RegisterAuthenticationRequest request);
}
