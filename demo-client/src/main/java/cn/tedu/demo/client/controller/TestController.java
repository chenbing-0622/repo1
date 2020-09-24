package cn.tedu.demo.client.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import javafx.beans.DefaultProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.List;
import java.util.Map;

@RestController
@DefaultProperties(defaultFallback = "getUserByIdFallBack")
public class TestController {
    @Autowired
    DiscoveryClient discoveryClient;

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand
    @RequestMapping("/test")
    public Map<String,String> test(){
        System.out.println("消费者");
        String url="http://localhost:8070/abc";
        Map<String,String> map=restTemplate.getForObject(url, Map.class);
        return map;
    }

    @RequestMapping("/registered")
    public String getRegistered(){
        List<ServiceInstance> list = discoveryClient.getInstances("STORES");
        //discoveryClient.getServices().size() = 1
        System.out.println("discoveryClient.getServices().size() = " + discoveryClient.getServices().size());
        for( String s :  discoveryClient.getServices()){
            //services api
            System.out.println("services " + s);
            List<ServiceInstance> serviceInstances =  discoveryClient.getInstances(s);
            for(ServiceInstance si : serviceInstances){
                //services:api:getHost()=localhost
                System.out.println("    services:" + s + ":getHost()=" + si.getHost());
                // services:api:getPort()=8090
                System.out.println("    services:" + s + ":getPort()=" + si.getPort());
                //services:api:getServiceId()=API
                System.out.println("    services:" + s + ":getServiceId()=" + si.getServiceId());
                //services:api:getUri()=http://localhost:8090
                System.out.println("    services:" + s + ":getUri()=" + si.getUri());
                //services:api:getMetadata()={management.port=8090}
                System.out.println("    services:" + s + ":getMetadata()=" + si.getMetadata());
            }
        }
        return "success";
    }
}
