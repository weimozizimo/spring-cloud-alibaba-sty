package com.wyf;

import com.wyf.util.dto.Person;
import com.wyf.util.queue.BlockQueue;
import com.wyf.util.queue.BlockQueueRunner;
import com.wyf.util.queue.wokers.PrintPersonNameWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@EnableDiscoveryClient
@SpringBootApplication
public class ConsumerApplication1 {



    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

        ConfigurableApplicationContext context = SpringApplication.run(ConsumerApplication1.class, args);

        BlockQueue queue = context.getBean(BlockQueue.class);
        int capacity = queue.curCapacity();
        System.out.println(capacity);
    }


    



    @RestController
    public class EchoController {

        @Autowired
        private LoadBalancerClient loadBalancerClient;

        @Value("${spring.application.name}")
        private String appName;

        @Autowired
        private RestTemplate restTemplate;

        @Autowired
        private BlockQueueRunner blockQueueRunner;

        @Value("${project.type}")
        private String type;


        @GetMapping(value = "/echo")
        public String echo() {
            ServiceInstance instance = loadBalancerClient.choose("service-provider");
            String path = String.format("http://%s:%s/echo/%s", instance.getHost(), instance.getPort(), appName);
            System.out.println("request path:" +path);
            return  restTemplate.getForObject(path,String.class);
        }

        @PostMapping("/addPrintNameTask")
        public void addPrintNameTask(@RequestBody List<Person> persons){
            persons.forEach(person -> {
                blockQueueRunner.addTask(new PrintPersonNameWorker(person.getGrade(),person.getName()));
            });
            blockQueueRunner.stopNow();
        }


        @GetMapping(value = "/type")
        public String type() {
            return type;
        }

    }

    //Instantiate RestTemplate Instance
    @Bean
    public RestTemplate restTemplate(){

        return new RestTemplate();
    }



}
