package stark.coderaider.titan.gate.api.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import stark.dataworks.boot.web.PaginationRequest;

@Data
@EqualsAndHashCode(callSuper = true)
public class ListBusinessSystemsRequest extends PaginationRequest
{
    @NotBlank(message = "Code cannot be blank.")
    private String code;

    @NotBlank(message = "Name cannot be blank.")
    private String name;
}