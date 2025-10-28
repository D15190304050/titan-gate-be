package stark.coderaider.titan.gate.core.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stark.coderaider.titan.gate.core.services.SsoService;
import stark.coderaider.titan.gate.loginstate.UserInfo;
import stark.dataworks.boot.web.ServiceResponse;

@Slf4j
@RestController
@RequestMapping("/sso")
public class SsoController
{
    @Autowired
    private SsoService ssoService;

    @PostMapping("/verify-token")
    public ServiceResponse<UserInfo> verifyToken(HttpServletRequest request)
    {
        return ssoService.verifyToken(request);
    }
}
