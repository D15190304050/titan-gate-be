package stark.coderaider.titan.gate.api.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class UserRoleUpdateRequest
{
    @Min(value = 1, message = "OperatorId must be positive.")
    private long operatorId;

    @Min(value = 1, message = "UserId must be positive.")
    private long userId;

    @NotBlank(message = "SystemCode is required.")
    private String systemCode;

    @NotEmpty(message = "RoleIds cannot be empty.")
    private List<Long> roleIds;
}
