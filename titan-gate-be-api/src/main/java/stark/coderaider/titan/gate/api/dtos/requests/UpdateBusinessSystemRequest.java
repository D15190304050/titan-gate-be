package stark.coderaider.titan.gate.api.dtos.requests;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateBusinessSystemRequest
{
    @Min(value = 1, message = "Id must be positive.")
    private long id;

    @Min(value = 1, message = "UserId must be positive.")
    private long userId;

    private String name;

    private String description;
}