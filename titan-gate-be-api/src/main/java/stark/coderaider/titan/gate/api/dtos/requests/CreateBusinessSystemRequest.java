package stark.coderaider.titan.gate.api.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateBusinessSystemRequest
{
    @NotBlank(message = "Code is required.")
    private String code;

    @NotBlank(message = "Name is required.")
    private String name;

    private String description;
}