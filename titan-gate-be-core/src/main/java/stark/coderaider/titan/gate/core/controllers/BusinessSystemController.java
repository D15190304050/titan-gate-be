package stark.coderaider.titan.gate.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import stark.coderaider.titan.gate.api.dtos.requests.CreateBusinessSystemRequest;
import stark.coderaider.titan.gate.api.dtos.requests.DeleteBusinessSystemsRequest;
import stark.coderaider.titan.gate.api.dtos.requests.ListBusinessSystemsRequest;
import stark.coderaider.titan.gate.api.dtos.requests.UpdateBusinessSystemRequest;
import stark.coderaider.titan.gate.api.dtos.responses.BusinessSystemResponse;
import stark.coderaider.titan.gate.core.services.BusinessSystemService;
import stark.dataworks.boot.web.ServiceResponse;

import java.util.List;

// TODO: Validate authorization here. Role ADMIN of titan_gate.
@RestController
@RequestMapping("/business-systems")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
public class BusinessSystemController
{
    @Autowired
    private BusinessSystemService businessSystemService;

    @PostMapping("/create")
    public ServiceResponse<BusinessSystemResponse> createBusinessSystem(@RequestBody CreateBusinessSystemRequest request)
    {
        return businessSystemService.createBusinessSystem(request);
    }

    @PostMapping("/update")
    public ServiceResponse<BusinessSystemResponse> updateBusinessSystem(@RequestBody UpdateBusinessSystemRequest request)
    {
        return businessSystemService.updateBusinessSystem(request);
    }

    @PostMapping("/delete")
    public ServiceResponse<Boolean> deleteBusinessSystems(@RequestBody DeleteBusinessSystemsRequest request)
    {
        return businessSystemService.deleteBusinessSystems(request);
    }

    @GetMapping("/list")
    public ServiceResponse<List<BusinessSystemResponse>> listBusinessSystems(@ModelAttribute ListBusinessSystemsRequest request)
    {
        return businessSystemService.listBusinessSystems(request);
    }
}