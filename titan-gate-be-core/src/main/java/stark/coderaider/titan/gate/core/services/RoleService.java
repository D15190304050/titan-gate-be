package stark.coderaider.titan.gate.core.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import stark.coderaider.titan.gate.api.IRoleService;
import stark.coderaider.titan.gate.api.dtos.requests.HasRoleRequest;
import stark.coderaider.titan.gate.api.dtos.requests.CreateRoleRequest;
import stark.coderaider.titan.gate.api.dtos.requests.DeleteRolesRequest;
import stark.coderaider.titan.gate.api.dtos.requests.ListRolesRequest;
import stark.coderaider.titan.gate.api.dtos.requests.UpdateRoleRequest;
import stark.coderaider.titan.gate.api.dtos.requests.UpdateUserRolesRequest;
import stark.coderaider.titan.gate.api.dtos.requests.GetUserRolesRequest;
import stark.coderaider.titan.gate.api.dtos.responses.RoleResponse;
import stark.coderaider.titan.gate.core.dao.RoleMapper;
import stark.coderaider.titan.gate.core.dao.UserRoleMapper;
import stark.coderaider.titan.gate.core.domain.entities.mysql.Role;
import stark.coderaider.titan.gate.core.domain.entities.mysql.UserRole;
import stark.coderaider.titan.gate.core.redis.TitanGateRedisOperation;
import stark.dataworks.boot.autoconfig.web.LogArgumentsAndResponse;
import stark.dataworks.boot.web.ServiceResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@DubboService
@Service
@Validated
@LogArgumentsAndResponse
public class RoleService implements IRoleService
{
    private static final String SUPER_ADMIN_CODE = "SUPER_ADMIN";
    private static final String SYSTEM_ADMIN_CODE = "SYSTEM_ADMIN";
    private static final long USER_ROLE_CACHE_MINUTES = 5;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private TitanGateRedisOperation titanGateRedisOperation;

    @Override
    public ServiceResponse<Boolean> hasRole(@Valid @NotNull HasRoleRequest request)
    {
        Set<String> userRoles = getUserRoleCodes(request.getUserId(), request.getSystemCode());
        return ServiceResponse.buildSuccessResponse(userRoles.contains(request.getRoleCode()));
    }

    @Override
    public ServiceResponse<Set<String>> getUserRoles(@Valid @NotNull GetUserRolesRequest request)
    {
        Set<String> roles = getUserRoleCodes(request.getUserId(), request.getSystemCode());
        return ServiceResponse.buildSuccessResponse(roles);
    }

    @Override
    public ServiceResponse<RoleResponse> createRole(@Valid @NotNull CreateRoleRequest request)
    {
        if (!isSuperAdmin(request.getOperatorId()) && !isSystemAdmin(request.getOperatorId(), request.getSystemCode()))
            return ServiceResponse.buildErrorResponse(-1, "Only super admin or system admin can create roles.");

        Role existing = roleMapper.getByCode(request.getCode());
        if (existing != null)
            return ServiceResponse.buildErrorResponse(-1, "Role code already exists.");

        Role role = new Role();
        role.setCode(request.getCode());
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role.setSystemCode(request.getSystemCode());
        role.setCreatorId(request.getOperatorId());
        role.setModifierId(request.getOperatorId());
        roleMapper.insert(role);

        return ServiceResponse.buildSuccessResponse(toResponse(role));
    }

    @Override
    public ServiceResponse<RoleResponse> updateRole(@Valid @NotNull UpdateRoleRequest request)
    {
        Role role = roleMapper.getById(request.getId());
        if (role == null)
            return ServiceResponse.buildErrorResponse(-1, "Role does not exist.");

        if (!isSuperAdmin(request.getOperatorId()) && !isSystemAdmin(request.getOperatorId(), role.getSystemCode()))
            return ServiceResponse.buildErrorResponse(-1, "Only super admin or system admin can update roles.");

        if (StringUtils.hasText(request.getName()))
            role.setName(request.getName());

        if (request.getDescription() != null)
            role.setDescription(request.getDescription());

        role.setModifierId(request.getOperatorId());
        roleMapper.update(role);

        return ServiceResponse.buildSuccessResponse(toResponse(role));
    }

    @Override
    public ServiceResponse<Boolean> deleteRoles(@Valid @NotNull DeleteRolesRequest request)
    {
        Set<Long> roleIds = new HashSet<>(request.getRoleIds());
        if (roleIds.isEmpty())
            return ServiceResponse.buildErrorResponse(-1, "RoleIds cannot be empty.");

        List<Role> roles = roleMapper.getByIds(roleIds);

        // Check if all roles exist.
        if (roles.size() != roleIds.size())
        {
            HashSet<Long> invalidRoleIds = new HashSet<>(roleIds);
            for (Role role : roles)
                invalidRoleIds.remove(role.getId());

            String invalidRoleIdsString = invalidRoleIds.stream().map(Object::toString).collect(Collectors.joining(", "));
            return ServiceResponse.buildErrorResponse(-1, "Some roles do not exist: " + invalidRoleIdsString + ".");
        }

        Set<String> systemsInScope = roles.stream().map(Role::getSystemCode).collect(Collectors.toSet());
        if (!isSuperAdmin(request.getOperatorId()) && !isAdminForSystems(request.getOperatorId(), systemsInScope))
            return ServiceResponse.buildErrorResponse(-1, "Only super admin or system admin can delete roles.");

        roleMapper.deleteByIds(roleIds);
        return ServiceResponse.buildSuccessResponse(true);
    }

    // TODO: According to this design, we should have limited roles for each system.
    @Override
    public ServiceResponse<List<RoleResponse>> listRoles(@Valid @NotNull ListRolesRequest request)
    {
        List<Role> roles;
        if (StringUtils.hasText(request.getSystemCode()))
            roles = roleMapper.listBySystem(request.getSystemCode());
        else
            roles = roleMapper.listAll();

        List<RoleResponse> response = roles.stream().map(RoleService::toResponse).collect(Collectors.toList());
        return ServiceResponse.buildSuccessResponse(response);
    }

    @Override
    public ServiceResponse<Boolean> updateUserRoles(@Valid @NotNull UpdateUserRolesRequest request)
    {
        // Validate whether the operator can update other users' roles.
        if (request.getOperatorId() != request.getUserId())
        {
            boolean allowed = isSuperAdmin(request.getOperatorId()) || isSystemAdmin(request.getOperatorId(), request.getSystemCode());
            if (!allowed)
                return ServiceResponse.buildErrorResponse(-1, "Only super admin or system admin can update roles for other users.");
        }

        Set<Long> roleIdSet = new HashSet<>(request.getRoleIds());

        // Allow clearing all roles under the system.
        if (roleIdSet.isEmpty())
        {
            userRoleMapper.deleteByUserAndSystem(request.getUserId(), request.getSystemCode());
            titanGateRedisOperation.cacheUserRoles(request.getUserId(), request.getSystemCode(), Collections.emptySet(), USER_ROLE_CACHE_MINUTES);

            return ServiceResponse.buildSuccessResponse(true);
        }

        List<Role> roles = roleMapper.getByIds(roleIdSet);
        if (roles.size() != roleIdSet.size())
            return ServiceResponse.buildErrorResponse(-1, "Some roles do not exist.");

        // Validate that all roles belong to the target system.
        boolean allMatchSystem = roles.stream().allMatch(r -> request.getSystemCode().equals(r.getSystemCode()));
        if (!allMatchSystem)
            return ServiceResponse.buildErrorResponse(-1, "All roles must belong to the provided system.");

        // Persist the role assignments for the user.
        Set<String> roleCodes = roles.stream().map(Role::getCode).collect(Collectors.toSet());
        String roleIdsString = roleIdSet.stream().map(String::valueOf).collect(Collectors.joining(","));
        UserRole existingUserRole = userRoleMapper.getByUserAndSystem(request.getUserId(), request.getSystemCode());
        if (existingUserRole == null)
        {
            UserRole newUserRole = new UserRole();
            newUserRole.setCreatorId(request.getOperatorId());
            newUserRole.setModifierId(request.getOperatorId());
            newUserRole.setUserId(request.getUserId());
            newUserRole.setSystemCode(request.getSystemCode());
            newUserRole.setRoleIds(roleIdsString);
            userRoleMapper.insert(newUserRole);
        }
        else
        {
            existingUserRole.setRoleIds(roleIdsString);
            existingUserRole.setModifierId(request.getOperatorId());
            existingUserRole.setSystemCode(request.getSystemCode());
            userRoleMapper.update(existingUserRole);
        }

        // Refresh the cache so future reads return the latest roles.
        titanGateRedisOperation.cacheUserRoles(request.getUserId(), request.getSystemCode(), roleCodes, USER_ROLE_CACHE_MINUTES);
        return ServiceResponse.buildSuccessResponse(true);
    }

    private static RoleResponse toResponse(Role role)
    {
        RoleResponse response = new RoleResponse();
        BeanUtils.copyProperties(role, response);
        return response;
    }

    private Set<String> getUserRoleCodes(long userId, String systemCode)
    {
        if (!StringUtils.hasText(systemCode))
            return getAllRoleCodes(userId);

        Set<String> cachedRoles = titanGateRedisOperation.getCachedUserRoles(userId, systemCode);
        if (cachedRoles != null)
            return cachedRoles;

        UserRole userRole = userRoleMapper.getByUserAndSystem(userId, systemCode);
        if (userRole == null || !StringUtils.hasText(userRole.getRoleIds()))
        {
            titanGateRedisOperation.cacheUserRoles(userId, systemCode, Collections.emptySet(), USER_ROLE_CACHE_MINUTES);
            return Collections.emptySet();
        }

        Set<Long> roleIds = parseRoleIds(userRole.getRoleIds());
        List<Role> roles = CollectionUtils.isEmpty(roleIds) ? Collections.emptyList() : roleMapper.getByIds(roleIds);
        Set<String> roleCodes = roles.stream().map(Role::getCode).collect(Collectors.toSet());
        titanGateRedisOperation.cacheUserRoles(userId, systemCode, roleCodes, USER_ROLE_CACHE_MINUTES);
        return roleCodes;
    }

    private Set<String> getAllRoleCodes(long userId)
    {
        List<UserRole> userRoles = userRoleMapper.listByUser(userId);
        if (CollectionUtils.isEmpty(userRoles))
            return Collections.emptySet();

        Set<Long> roleIds = new HashSet<>();
        for (UserRole userRole : userRoles)
        {
            Set<Long> parsedIds = parseRoleIds(userRole.getRoleIds());
            roleIds.addAll(parsedIds);
        }

        if (roleIds.isEmpty())
            return Collections.emptySet();

        List<Role> roles = roleMapper.getByIds(roleIds);
        Map<Long, Role> roleMap = roles.stream().collect(Collectors.toMap(Role::getId, r -> r));

        return roleIds.stream().map(roleMap::get).filter(Objects::nonNull).map(Role::getCode).collect(Collectors.toSet());
    }

    private Set<Long> parseRoleIds(String roleIds)
    {
        if (!StringUtils.hasText(roleIds))
            return Collections.emptySet();

        String[] segments = roleIds.split(",");
        Set<Long> ids = new HashSet<>();
        for (String segment : segments)
        {
            if (StringUtils.hasText(segment))
                ids.add(Long.parseLong(segment));
        }

        return ids;
    }

    private boolean isSuperAdmin(long userId)
    {
        return getAllRoleCodes(userId).contains(SUPER_ADMIN_CODE);
    }

    private boolean isSystemAdmin(long userId, String systemCode)
    {
        if (!StringUtils.hasText(systemCode))
            return false;

        return getUserRoleCodes(userId, systemCode).contains(SYSTEM_ADMIN_CODE);
    }

    private boolean isAdminForSystems(long userId, Set<String> systemCodes)
    {
        if (CollectionUtils.isEmpty(systemCodes))
            return false;

        for (String systemCode : systemCodes)
        {
            if (!isSystemAdmin(userId, systemCode))
                return false;
        }

        return true;
    }
}
