package stark.coderaider.titan.gate.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import stark.coderaider.titan.gate.api.dtos.requests.HasRoleRequest;
import stark.coderaider.titan.gate.api.dtos.requests.RoleCreateRequest;
import stark.coderaider.titan.gate.api.dtos.requests.RoleDeleteRequest;
import stark.coderaider.titan.gate.api.dtos.requests.RoleQueryRequest;
import stark.coderaider.titan.gate.api.dtos.requests.RoleUpdateRequest;
import stark.coderaider.titan.gate.api.dtos.requests.UserRoleUpdateRequest;
import stark.coderaider.titan.gate.api.dtos.requests.UserRolesQueryRequest;
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
    ServiceResponse<Set<String>> getUserRoles(@Valid @NotNull UserRolesQueryRequest request);

    /**
     * Create a new role.
     */
    ServiceResponse<RoleResponse> createRole(@Valid @NotNull RoleCreateRequest request);

    /**
     * Update basic role information.
     */
    ServiceResponse<RoleResponse> updateRole(@Valid @NotNull RoleUpdateRequest request);

    /**
     * Delete one or more roles.
     */
    ServiceResponse<Boolean> deleteRoles(@Valid @NotNull RoleDeleteRequest request);

    /**
     * Query roles by optional system code.
     */
    ServiceResponse<List<RoleResponse>> listRoles(@Valid @NotNull RoleQueryRequest request);

    /**
     * Set roles for a user in a system using an overwrite strategy.
     */
    ServiceResponse<Boolean> updateUserRoles(@Valid @NotNull UserRoleUpdateRequest request);
}
