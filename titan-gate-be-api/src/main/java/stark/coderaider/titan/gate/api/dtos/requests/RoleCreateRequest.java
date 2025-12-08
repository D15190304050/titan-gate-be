package stark.coderaider.titan.gate.api.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleCreateRequest
{
    @Min(value = 1, message = "OperatorId must be positive.")
    private long operatorId;

    @NotBlank(message = "Code is required.")
    private String code;

    @NotBlank(message = "Name is required.")
    private String name;

    private String description;

    @NotBlank(message = "SystemCode is required.")
    private String systemCode;
}
