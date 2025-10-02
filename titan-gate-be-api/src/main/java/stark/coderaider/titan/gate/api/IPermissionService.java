package stark.coderaider.titan.gate.api;

import stark.coderaider.titan.gate.api.dtos.responses.AuthResult;

import java.util.Set;

public interface IPermissionService
{
    /**
     * 检查用户是否有指定角色
     */
    AuthResult<Boolean> hasRole(Long userId, String roleCode);

    /**
     * 检查用户是否有指定权限
     */
    AuthResult<Boolean> hasPermission(Long userId, String permission);

    /**
     * 获取用户所有角色
     */
    AuthResult<Set<String>> getUserRoles(Long userId);

    /**
     * 获取用户所有权限
     */
    AuthResult<Set<String>> getUserPermissions(Long userId);
}