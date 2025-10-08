package stark.coderaider.titan.gate.api.dtos.requests;

import lombok.Data;

import java.io.Serializable;

@Data
public class RegisterAuthenticationRequest implements Serializable
{
    private String username;
    private String password;
    private String email;
    private String phoneNumberCountryCode;
    private String phoneNumber;
}
