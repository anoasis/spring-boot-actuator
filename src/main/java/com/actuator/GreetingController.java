package com.actuator;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kangj on 1/26/2018.
 */

@RestController
public class GreetingController {

    @Autowired
    private MeterRegistry influxMeterRegistry;

    @PostConstruct
    public void setCommonTags() {
        influxMeterRegistry.config().commonTags("env", "IPE", "app", "poc-actuator");
    }

    private Counter hellos = Metrics.counter("hellos", "env", "IPE", "app", "poc-actuator");

    @Timed
    @GetMapping("/hello")
    public Map<String,String> hello() {
        hellos.increment();
        return new HashMap<String, String>() {{ put("hello", "world"); }};
    }


    private Counter byes = Metrics.counter("byes", "env", "IPE", "app", "poc-actuator");

    @Timed
    @GetMapping("/bye")
    public Map<String,String> bye() {
        byes.increment();
        return new HashMap<String, String>() {{ put("bye", "world"); }};
    }

}
