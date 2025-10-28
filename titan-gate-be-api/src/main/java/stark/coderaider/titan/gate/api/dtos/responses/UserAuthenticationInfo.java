package stark.coderaider.titan.gate.api.dtos.responses;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserAuthenticationInfo implements Serializable
{
    private String username;
    private String email;
    private String phoneNumberCountryCode;
    private String phoneNumber;
}
