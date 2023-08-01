package com.wyf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@EnableDiscoveryClient
@SpringBootApplication
public class ProviderApplication1 {

    public static void main(String[] args) {

        ConfigurableApplicationContext applicationContext = SpringApplication.run(ProviderApplication1.class, args);
        System.out.println("project.type is :"+applicationContext.getEnvironment().getProperty("project.type"));
    }

    @Value("${server.port}")
    private String port;

    @Value("${project.type}")
    private String type;

    @RestController
    public class EchoController {
        @GetMapping(value = "/echo/{string}")
        public String echo(@PathVariable String string) {
            return "Hello Nacos Discovery,my port is "+port+" " + string;
        }

        @GetMapping(value = "/type")
        public String type() {
        	return type;
        }
    }
}
