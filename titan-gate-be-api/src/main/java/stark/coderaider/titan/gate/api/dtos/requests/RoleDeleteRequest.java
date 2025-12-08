package stark.coderaider.titan.gate.api.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class RoleDeleteRequest
{
    @Min(value = 1, message = "OperatorId must be positive.")
    private long operatorId;

    @NotEmpty(message = "RoleIds cannot be empty.")
    private List<@NotNull(message = "RoleId cannot be null.") Long> roleIds;
}
