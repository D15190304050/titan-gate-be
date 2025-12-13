package stark.coderaider.titan.gate.api.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class DeleteBusinessSystemsRequest
{
    @NotEmpty(message = "SystemIds cannot be empty.")
    private List<@NotNull(message = "SystemId cannot be null.") Long> systemIds;
}