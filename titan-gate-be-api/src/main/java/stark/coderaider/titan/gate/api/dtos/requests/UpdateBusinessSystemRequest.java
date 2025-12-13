package stark.coderaider.titan.gate.api.dtos.requests;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateBusinessSystemRequest
{
    @Min(value = 1, message = "Id must be positive.")
    private long id;

    private String name;

    private String description;
}