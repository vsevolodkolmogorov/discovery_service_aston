package ru.astondevs.discoveryserviceaston.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.astondevs.discoveryserviceaston.dto.InstanceDto;
import ru.astondevs.discoveryserviceaston.model.RegisteredInstance;
import ru.astondevs.discoveryserviceaston.service.ServiceRegistry;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/services")
public class DiscoveryController {

    private final ServiceRegistry registry;

    public DiscoveryController(ServiceRegistry registry) {
        this.registry = registry;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody InstanceDto dto) {
        registry.register(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/heartbeat")
    public ResponseEntity<Void> heartbeat(@RequestBody Map<String, String> body) {
        String instanceId = body.get("instanceId");
        if (instanceId == null) return ResponseEntity.badRequest().build();
        registry.heartbeat(instanceId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deregister/{instanceId}")
    public ResponseEntity<Void> deregister(@PathVariable String instanceId) {
        registry.deregister(instanceId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{serviceName}")
    public ResponseEntity<List<RegisteredInstance>> getInstances(@PathVariable String serviceName) {
        return ResponseEntity.ok(registry.getInstances(serviceName));
    }

    @GetMapping
    public ResponseEntity<ConcurrentHashMap<String, ConcurrentHashMap<String, RegisteredInstance>>> getAll() {
        return ResponseEntity.ok(registry.getAll());
    }

}
