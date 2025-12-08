package stark.coderaider.titan.gate.core.services;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import stark.coderaider.titan.gate.api.IRoleService;
import stark.coderaider.titan.gate.api.dtos.requests.HasRoleRequest;
import stark.coderaider.titan.gate.api.dtos.requests.RoleCreateRequest;
import stark.coderaider.titan.gate.api.dtos.requests.RoleDeleteRequest;
import stark.coderaider.titan.gate.api.dtos.requests.RoleQueryRequest;
import stark.coderaider.titan.gate.api.dtos.requests.RoleUpdateRequest;
import stark.coderaider.titan.gate.api.dtos.requests.UserRoleUpdateRequest;
import stark.coderaider.titan.gate.api.dtos.requests.UserRolesQueryRequest;
import stark.coderaider.titan.gate.api.dtos.responses.RoleResponse;
import stark.coderaider.titan.gate.core.dao.RoleMapper;
import stark.coderaider.titan.gate.core.dao.UserRoleMapper;
import stark.coderaider.titan.gate.core.domain.entities.mysql.Role;
import stark.coderaider.titan.gate.core.domain.entities.mysql.UserRole;
import stark.coderaider.titan.gate.core.redis.TitanGateRedisOperation;
import stark.dataworks.boot.web.ServiceResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@DubboService
@Service
@Validated
public class RoleService implements IRoleService
{
    private static final String SUPER_ADMIN_CODE = "SUPER_ADMIN";
    private static final String SYSTEM_ADMIN_CODE = "SYSTEM_ADMIN";
    private static final long USER_ROLE_CACHE_MINUTES = 30;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private TitanGateRedisOperation titanGateRedisOperation;

    @Override
    public ServiceResponse<Boolean> hasRole(@Valid @NotNull HasRoleRequest request)
    {
        Set<String> userRoles = getUserRolesFromDatabase(request.getUserId(), request.getSystemCode(), true);
        return ServiceResponse.buildSuccessResponse(userRoles.contains(request.getRoleCode()));
    }

    @Override
    public ServiceResponse<Set<String>> getUserRoles(@Valid @NotNull UserRolesQueryRequest request)
    {
        Set<String> roles = getUserRolesFromDatabase(request.getUserId(), request.getSystemCode(), true);
        return ServiceResponse.buildSuccessResponse(roles);
    }

    @Override
    public ServiceResponse<RoleResponse> createRole(@Valid @NotNull RoleCreateRequest request)
    {
        if (!isSuperAdmin(request.getOperatorId()) && !isSystemAdmin(request.getOperatorId(), request.getSystemCode()))
        {
            return ServiceResponse.buildErrorResponse(-1, "Only super admin or system admin can create roles.");
        }

        Role existing = roleMapper.getByCode(request.getCode());
        if (existing != null)
        {
            return ServiceResponse.buildErrorResponse(-1, "Role code already exists.");
        }

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
    public ServiceResponse<RoleResponse> updateRole(@Valid @NotNull RoleUpdateRequest request)
    {
        Role role = roleMapper.getById(request.getId());
        if (role == null)
        {
            return ServiceResponse.buildErrorResponse(-1, "Role does not exist.");
        }

        if (!isSuperAdmin(request.getOperatorId()) && !isSystemAdmin(request.getOperatorId(), role.getSystemCode()))
        {
            return ServiceResponse.buildErrorResponse(-1, "Only super admin or system admin can update roles.");
        }

        if (StringUtils.hasText(request.getName()))
        {
            role.setName(request.getName());
        }
        if (request.getDescription() != null)
        {
            role.setDescription(request.getDescription());
        }
        role.setModifierId(request.getOperatorId());
        roleMapper.update(role);

        return ServiceResponse.buildSuccessResponse(toResponse(role));
    }

    @Override
    public ServiceResponse<Boolean> deleteRoles(@Valid @NotNull RoleDeleteRequest request)
    {
        Set<Long> roleIds = new HashSet<>(request.getRoleIds());
        if (roleIds.isEmpty())
        {
            return ServiceResponse.buildErrorResponse(-1, "RoleIds cannot be empty.");
        }

        List<Role> roles = roleMapper.getByIds(roleIds);
        if (roles.size() != roleIds.size())
        {
            return ServiceResponse.buildErrorResponse(-1, "Some roles do not exist.");
        }

        Set<String> systemsInScope = roles.stream().map(Role::getSystemCode).collect(Collectors.toSet());
        if (!isSuperAdmin(request.getOperatorId()) && !isAdminForSystems(request.getOperatorId(), systemsInScope))
        {
            return ServiceResponse.buildErrorResponse(-1, "Only super admin or system admin can delete roles.");
        }

        roleMapper.deleteByIds(roleIds);
        return ServiceResponse.buildSuccessResponse(true);
    }

    @Override
    public ServiceResponse<List<RoleResponse>> listRoles(@Valid @NotNull RoleQueryRequest request)
    {
        List<Role> roles;
        if (StringUtils.hasText(request.getSystemCode()))
        {
            roles = roleMapper.listBySystem(request.getSystemCode());
        }
        else
        {
            roles = roleMapper.listAll();
        }
        List<RoleResponse> response = roles.stream().map(this::toResponse).collect(Collectors.toList());
        return ServiceResponse.buildSuccessResponse(response);
    }

    @Override
    public ServiceResponse<Boolean> updateUserRoles(@Valid @NotNull UserRoleUpdateRequest request)
    {
        Set<Long> roleIdSet = new HashSet<>(request.getRoleIds());
        if (roleIdSet.isEmpty())
        {
            return ServiceResponse.buildErrorResponse(-1, "RoleIds cannot be empty.");
        }

        List<Role> roles = roleMapper.getByIds(roleIdSet);
        if (roles.size() != roleIdSet.size())
        {
            return ServiceResponse.buildErrorResponse(-1, "Some roles do not exist.");
        }

        boolean allMatchSystem = roles.stream().allMatch(r -> request.getSystemCode().equals(r.getSystemCode()));
        if (!allMatchSystem)
        {
            return ServiceResponse.buildErrorResponse(-1, "All roles must belong to the provided system.");
        }

        if (request.getOperatorId() != request.getUserId())
        {
            boolean allowed = isSuperAdmin(request.getOperatorId()) || isSystemAdmin(request.getOperatorId(), request.getSystemCode());
            if (!allowed)
            {
                return ServiceResponse.buildErrorResponse(-1, "Only super admin or system admin can update roles for other users.");
            }
        }

        String roleIds = request.getRoleIds().stream().map(String::valueOf).collect(Collectors.joining(","));
        UserRole existing = userRoleMapper.getByUserAndSystem(request.getUserId(), request.getSystemCode());
        if (existing == null)
        {
            existing = new UserRole();
            existing.setCreatorId(request.getOperatorId());
            existing.setModifierId(request.getOperatorId());
            existing.setUserId(request.getUserId());
            existing.setSystemCode(request.getSystemCode());
            existing.setRoleIds(roleIds);
            userRoleMapper.insert(existing);
        }
        else
        {
            existing.setRoleIds(roleIds);
            existing.setModifierId(request.getOperatorId());
            existing.setSystemCode(request.getSystemCode());
            userRoleMapper.update(existing);
        }

        titanGateRedisOperation.removeCachedUserRoles(request.getUserId(), request.getSystemCode());
        return ServiceResponse.buildSuccessResponse(true);
    }

    private RoleResponse toResponse(Role role)
    {
        RoleResponse response = new RoleResponse();
        response.setId(role.getId());
        response.setCode(role.getCode());
        response.setName(role.getName());
        response.setDescription(role.getDescription());
        response.setSystemCode(role.getSystemCode());
        return response;
    }

    private Set<String> getUserRolesFromDatabase(long userId, String systemCode, boolean useCache)
    {
        if (!StringUtils.hasText(systemCode))
        {
            return getAllRoleCodes(userId);
        }

        Set<String> cached = useCache ? titanGateRedisOperation.getCachedUserRoles(userId, systemCode) : null;
        if (cached != null)
        {
            return cached;
        }

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
        {
            return Collections.emptySet();
        }

        Set<Long> roleIds = new HashSet<>();
        Map<String, Set<Long>> roleIdsBySystem = new HashMap<>();
        for (UserRole userRole : userRoles)
        {
            Set<Long> parsedIds = parseRoleIds(userRole.getRoleIds());
            roleIds.addAll(parsedIds);
            roleIdsBySystem.computeIfAbsent(userRole.getSystemCode(), key -> new HashSet<>()).addAll(parsedIds);
        }

        if (roleIds.isEmpty())
        {
            return Collections.emptySet();
        }

        List<Role> roles = roleMapper.getByIds(roleIds);
        Map<Long, Role> roleMap = roles.stream().collect(Collectors.toMap(Role::getId, r -> r));
        Set<String> codes = new HashSet<>();
        for (UserRole userRole : userRoles)
        {
            Set<Long> parsedIds = roleIdsBySystem.getOrDefault(userRole.getSystemCode(), Collections.emptySet());
            for (Long id : parsedIds)
            {
                Role role = roleMap.get(id);
                if (role != null)
                {
                    codes.add(role.getCode());
                }
            }
        }
        return codes;
    }

    private Set<Long> parseRoleIds(String roleIds)
    {
        if (!StringUtils.hasText(roleIds))
        {
            return Collections.emptySet();
        }
        String[] segments = roleIds.split(",");
        Set<Long> ids = new HashSet<>();
        for (String segment : segments)
        {
            if (StringUtils.hasText(segment))
            {
                ids.add(Long.parseLong(segment));
            }
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
        {
            return false;
        }
        return getUserRolesFromDatabase(userId, systemCode, true).contains(SYSTEM_ADMIN_CODE);
    }

    private boolean isAdminForSystems(long userId, Set<String> systemCodes)
    {
        if (CollectionUtils.isEmpty(systemCodes))
        {
            return false;
        }
        for (String systemCode : systemCodes)
        {
            if (!isSystemAdmin(userId, systemCode))
            {
                return false;
            }
        }
        return true;
    }
}
