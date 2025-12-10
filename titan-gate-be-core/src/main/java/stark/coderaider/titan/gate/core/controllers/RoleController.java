package stark.coderaider.titan.gate.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import stark.coderaider.titan.gate.api.dtos.requests.CreateRoleRequest;
import stark.coderaider.titan.gate.api.dtos.requests.DeleteRolesRequest;
import stark.coderaider.titan.gate.api.dtos.requests.ListRolesRequest;
import stark.coderaider.titan.gate.api.dtos.requests.UpdateRoleRequest;
import stark.coderaider.titan.gate.api.dtos.requests.UpdateUserRolesRequest;
import stark.coderaider.titan.gate.api.dtos.requests.GetUserRolesRequest;
import stark.coderaider.titan.gate.api.dtos.responses.RoleResponse;
import stark.coderaider.titan.gate.core.services.RoleService;
import stark.dataworks.boot.web.ServiceResponse;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/roles")
@Validated
public class RoleController
{
    @Autowired
    private RoleService roleService;

    @PostMapping("/create")
    public ServiceResponse<RoleResponse> createRole(@RequestBody CreateRoleRequest request)
    {
        return roleService.createRole(request);
    }

    @PostMapping("/update")
    public ServiceResponse<RoleResponse> updateRole(@RequestBody UpdateRoleRequest request)
    {
        return roleService.updateRole(request);
    }

    @PostMapping("/delete")
    public ServiceResponse<Boolean> deleteRoles(@RequestBody DeleteRolesRequest request)
    {
        return roleService.deleteRoles(request);
    }

    @GetMapping("/list")
    public ServiceResponse<List<RoleResponse>> listRoles(@ModelAttribute ListRolesRequest request)
    {
        return roleService.listRoles(request);
    }

    @PostMapping("/user-roles")
    public ServiceResponse<Boolean> updateUserRoles(@RequestBody UpdateUserRolesRequest request)
    {
        return roleService.updateUserRoles(request);
    }

    @GetMapping("/user-roles")
    public ServiceResponse<Set<String>> getUserRoles(@ModelAttribute GetUserRolesRequest request)
    {
        return roleService.getUserRoles(request);
    }
}
