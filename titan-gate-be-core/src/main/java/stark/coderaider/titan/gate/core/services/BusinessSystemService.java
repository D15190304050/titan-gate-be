package stark.coderaider.titan.gate.core.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import stark.coderaider.titan.gate.api.dtos.requests.CreateBusinessSystemRequest;
import stark.coderaider.titan.gate.api.dtos.requests.DeleteBusinessSystemsRequest;
import stark.coderaider.titan.gate.api.dtos.requests.ListBusinessSystemsRequest;
import stark.coderaider.titan.gate.api.dtos.requests.UpdateBusinessSystemRequest;
import stark.coderaider.titan.gate.api.dtos.responses.BusinessSystemResponse;
import stark.coderaider.titan.gate.core.dao.BusinessSystemMapper;
import stark.coderaider.titan.gate.core.domain.entities.mysql.BusinessSystem;
import stark.coderaider.titan.gate.loginstate.UserContextService;
import stark.dataworks.boot.autoconfig.web.LogArgumentsAndResponse;
import stark.dataworks.boot.web.ServiceResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@DubboService
@Service
@Validated
@LogArgumentsAndResponse
public class BusinessSystemService
{
    @Autowired
    private BusinessSystemMapper businessSystemMapper;

    @Transactional(rollbackFor = Exception.class)
    public ServiceResponse<BusinessSystemResponse> createBusinessSystem(@Valid @NotNull CreateBusinessSystemRequest request)
    {
        long currentUserId = UserContextService.getCurrentUserId();

        boolean exists = businessSystemMapper.existsByCodeOrName(request.getCode(), request.getName());
        if (exists)
            return ServiceResponse.buildErrorResponse(-1, "Business system code already exists.");

        BusinessSystem businessSystem = new BusinessSystem();
        businessSystem.setCode(request.getCode());
        businessSystem.setName(request.getName());
        businessSystem.setDescription(request.getDescription());
        businessSystem.setCreatorId(currentUserId);
        businessSystem.setModifierId(currentUserId);
        businessSystemMapper.insert(businessSystem);

        return ServiceResponse.buildSuccessResponse(toResponse(businessSystem));
    }

    @Transactional(rollbackFor = Exception.class)
    public ServiceResponse<BusinessSystemResponse> updateBusinessSystem(@Valid @NotNull UpdateBusinessSystemRequest request)
    {
        BusinessSystem businessSystem = businessSystemMapper.getById(request.getId());
        if (businessSystem == null)
            return ServiceResponse.buildErrorResponse(-1, "Business system does not exist.");

        if (StringUtils.hasText(request.getName()))
            businessSystem.setName(request.getName());

        if (request.getDescription() != null)
            businessSystem.setDescription(request.getDescription());

        businessSystem.setModifierId(request.getUserId());
        businessSystemMapper.updateById(businessSystem);

        return ServiceResponse.buildSuccessResponse(toResponse(businessSystem));
    }

    @Transactional(rollbackFor = Exception.class)
    public ServiceResponse<Boolean> deleteBusinessSystems(@Valid @NotNull DeleteBusinessSystemsRequest request)
    {
        Set<Long> systemIds = new HashSet<>(request.getSystemIds());
        if (systemIds.isEmpty())
            return ServiceResponse.buildErrorResponse(-1, "SystemIds cannot be empty.");

        List<BusinessSystem> systems = businessSystemMapper.getByIds(systemIds);

        // Check if all systems exist.
        if (systems.size() != systemIds.size())
        {
            HashSet<Long> invalidSystemIds = new HashSet<>(systemIds);
            for (BusinessSystem system : systems)
                invalidSystemIds.remove(system.getId());

            String invalidSystemIdsString = invalidSystemIds.stream().map(Object::toString).collect(Collectors.joining(", "));
            return ServiceResponse.buildErrorResponse(-1, "Some systems do not exist: " + invalidSystemIdsString + ".");
        }

        businessSystemMapper.deleteByIds(systemIds);
        return ServiceResponse.buildSuccessResponse(true);
    }

    public ServiceResponse<List<BusinessSystemResponse>> listBusinessSystems(@Valid @NotNull ListBusinessSystemsRequest request)
    {
        request.calculateLimitOffset();
        List<BusinessSystem> systems = businessSystemMapper.getBusinessSystems(request);
        List<BusinessSystemResponse> response = systems.stream().map(BusinessSystemService::toResponse).collect(Collectors.toList());
        return ServiceResponse.buildSuccessResponse(response);
    }

    private static BusinessSystemResponse toResponse(BusinessSystem businessSystem)
    {
        BusinessSystemResponse response = new BusinessSystemResponse();
        BeanUtils.copyProperties(businessSystem, response);
        return response;
    }
}