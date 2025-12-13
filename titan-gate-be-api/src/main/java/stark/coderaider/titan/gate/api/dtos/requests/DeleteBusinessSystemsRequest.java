package stark.coderaider.titan.gate.api.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class DeleteBusinessSystemsRequest
{
    @Min(value = 1, message = "UserId must be positive.")
    private long userId;

    @NotEmpty(message = "SystemIds cannot be empty.")
    private List<@NotNull(message = "SystemId cannot be null.") Long> systemIds;
}