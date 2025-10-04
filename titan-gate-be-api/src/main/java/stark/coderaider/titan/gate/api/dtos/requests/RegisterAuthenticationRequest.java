package stark.coderaider.titan.gate.api.dtos.requests;

import lombok.Data;

@Data
public class RegisterAuthenticationRequest
{
    private String username;
    private String encryptedPassword;
    private String email;
    private String phoneNumberCountryCode;
    private String phoneNumber;
}
