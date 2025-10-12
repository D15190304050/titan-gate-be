package stark.coderaider.titan.gate.core;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"stark.coderaider.titan.gate", "stark.dataworks.boot.autoconfig"})
@EnableDubbo
public class TitanGateMain
{
    public static void main(String[] args)
    {
        SpringApplication.run(TitanGateMain.class, args);
    }
}