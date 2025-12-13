package stark.coderaider.titan.gate.api.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateBusinessSystemRequest
{
    @Min(value = 1, message = "UserId must be positive.")
    private long userId;

    @NotBlank(message = "Code is required.")
    private String code;

    @NotBlank(message = "Name is required.")
    private String name;

    private String description;
}