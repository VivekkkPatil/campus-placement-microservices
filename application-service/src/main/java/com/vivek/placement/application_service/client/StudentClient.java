package com.vivek.placement.application_service.client;

import com.vivek.placement.application_service.dto.StudentDto;
import com.vivek.placement.application_service.exception.DownstreamUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Slf4j
@Component
public class StudentClient {

    private static final String SERVICE = "student-service";

    private final LoadBalancerClient loadBalancer;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;
    private final RestClient rest;

    public StudentClient(LoadBalancerClient loadBalancer, CircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this.loadBalancer = loadBalancer;
        this.circuitBreakerFactory = circuitBreakerFactory;
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory();
        factory.setReadTimeout(Duration.ofSeconds(3));
        this.rest = RestClient.builder().requestFactory(factory).build();
    }

    /** Returns null when the student does not exist. Fallback runs when the service is down. */
    public StudentDto getStudent(Long id) {
        return circuitBreakerFactory.create(SERVICE).run(
                () -> {
                    ServiceInstance instance = loadBalancer.choose(SERVICE);
                    if (instance == null) throw new IllegalStateException("No instance of " + SERVICE + " in Eureka");
                    return rest.get().uri(instance.getUri() + "/students/{id}", id).exchange((req, res) -> {
                        if (res.getStatusCode().value() == 404) return null;
                        if (res.getStatusCode().is2xxSuccessful()) return res.bodyTo(StudentDto.class);
                        throw new IllegalStateException(SERVICE + " returned " + res.getStatusCode());
                    });
                },
                throwable -> {
                    log.error("Call to {} failed", SERVICE, throwable);
                    throw new DownstreamUnavailableException(SERVICE);
                });
    }
}
