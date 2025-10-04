package stark.coderaider.titan.gate.api;

import stark.coderaider.titan.gate.api.dtos.requests.RegisterAuthenticationRequest;
import stark.dataworks.boot.web.ServiceResponse;

public interface IAuthenticationService
{
    ServiceResponse<Boolean> registerAuthentication(RegisterAuthenticationRequest request);
}
