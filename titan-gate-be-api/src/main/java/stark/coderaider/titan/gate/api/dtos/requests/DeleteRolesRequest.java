package stark.coderaider.titan.gate.api.dtos.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class DeleteRolesRequest
{
    @NotEmpty(message = "RoleIds cannot be empty.")
    private List<@NotNull(message = "RoleId cannot be null.") Long> roleIds;
}
