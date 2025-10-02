package stark.coderaider.titan.gate.api.dtos.responses;

import lombok.Data;

@Data
public class AuthResult<T>
{
    private boolean success;
    private String message;
    private T data;

    public static <T> AuthResult<T> success(T data)
    {
        AuthResult<T> result = new AuthResult<>();
        result.success = true;
        result.data = data;
        return result;
    }

    public static <T> AuthResult<T> error(String message)
    {
        AuthResult<T> result = new AuthResult<>();
        result.success = false;
        result.message = message;
        return result;
    }
}