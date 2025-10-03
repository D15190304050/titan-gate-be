package stark.coderaider.titan.gate.core.controllers;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stark.coderaider.titan.treasure.api.IConnectionTestService;

@Slf4j
@RestController
@RequestMapping("/connection")
public class ConnectionTestController
{
    @DubboReference(url = "${dubbo.service.profile.url}")
    private IConnectionTestService connectionTestService;

    @GetMapping("/hello")
    public String hello()
    {
        return "Hello";
    }

    @GetMapping("/say/dubbo")
    public String sayHelloByDubbo(String name)
    {
        return connectionTestService.sayHello(name);
    }
}
