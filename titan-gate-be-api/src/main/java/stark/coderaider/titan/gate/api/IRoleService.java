package stark.coderaider.titan.gate.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import stark.coderaider.titan.gate.api.dtos.requests.HasRoleRequest;
import stark.coderaider.titan.gate.api.dtos.requests.CreateRoleRequest;
import stark.coderaider.titan.gate.api.dtos.requests.DeleteRolesRequest;
import stark.coderaider.titan.gate.api.dtos.requests.ListRolesRequest;
import stark.coderaider.titan.gate.api.dtos.requests.UpdateRoleRequest;
import stark.coderaider.titan.gate.api.dtos.requests.UpdateUserRolesRequest;
import stark.coderaider.titan.gate.api.dtos.requests.GetUserRolesRequest;
import stark.coderaider.titan.gate.api.dtos.responses.RoleResponse;
import stark.dataworks.boot.web.ServiceResponse;

import java.util.List;
import java.util.Set;

public interface IRoleService
{
    /**
     * Check whether the user has the specified role.
     */
    ServiceResponse<Boolean> hasRole(@Valid @NotNull HasRoleRequest request);

    /**
     * Get all role codes for the user within the target system.
     */
    ServiceResponse<Set<String>> getUserRoles(@Valid @NotNull GetUserRolesRequest request);

    /**
     * Query roles by optional system code.
     */
    ServiceResponse<List<RoleResponse>> listRoles(@Valid @NotNull ListRolesRequest request);
}
