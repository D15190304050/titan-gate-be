package stark.coderaider.titan.gate.api.dtos.responses;

import lombok.Data;

@Data
public class BusinessSystemResponse
{
    private long id;
    private String code;
    private String name;
    private String description;
}