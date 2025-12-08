package stark.coderaider.titan.gate.api.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HasRoleRequest
{
    @Min(value = 1, message = "UserId must be positive.")
    private long userId;

    @NotBlank(message = "RoleCode is required.")
    private String roleCode;

    @NotBlank(message = "SystemCode is required.")
    private String systemCode;
}
