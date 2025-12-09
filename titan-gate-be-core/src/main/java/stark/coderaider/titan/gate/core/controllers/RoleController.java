package stark.coderaider.titan.gate.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @PostMapping
    public ServiceResponse<RoleResponse> create(@RequestBody CreateRoleRequest request)
    {
        return roleService.createRole(request);
    }

    @PutMapping("/{id}")
    public ServiceResponse<RoleResponse> update(@PathVariable("id") long id,
                                                @RequestBody UpdateRoleRequest request)
    {
        request.setId(id);
        return roleService.updateRole(request);
    }

    @DeleteMapping("/{id}")
    public ServiceResponse<Boolean> delete(@PathVariable("id") long id, @RequestParam("operatorId") long operatorId)
    {
        DeleteRolesRequest request = new DeleteRolesRequest();
        request.setOperatorId(operatorId);
        request.setRoleIds(java.util.Collections.singletonList(id));
        return roleService.deleteRoles(request);
    }

    @DeleteMapping
    public ServiceResponse<Boolean> batchDelete(@RequestBody DeleteRolesRequest request)
    {
        return roleService.deleteRoles(request);
    }

    @GetMapping
    public ServiceResponse<List<RoleResponse>> list(ListRolesRequest request)
    {
        return roleService.listRoles(request);
    }

    @PostMapping("/user")
    public ServiceResponse<Boolean> bindUser(@RequestBody UpdateUserRolesRequest request)
    {
        return roleService.updateUserRoles(request);
    }

    @GetMapping("/user/{userId}")
    public ServiceResponse<Set<String>> userRoles(@PathVariable("userId") long userId,
                                                 @RequestParam("systemCode") String systemCode)
    {
        GetUserRolesRequest request = new GetUserRolesRequest();
        request.setUserId(userId);
        request.setSystemCode(systemCode);
        return roleService.getUserRoles(request);
    }
}
