package stark.coderaider.titan.gate.api.dtos.responses;

import lombok.Data;

@Data
public class UserAuthenticationInfo
{
    private String username;
    private String email;
    private String phoneNumberCountryCode;
    private String phoneNumber;
}
