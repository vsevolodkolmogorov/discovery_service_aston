package ru.astondevs.discoveryserviceaston.service;

import org.springframework.stereotype.Service;
import ru.astondevs.discoveryserviceaston.dto.InstanceDto;
import ru.astondevs.discoveryserviceaston.model.RegisteredInstance;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ServiceRegistry {

    // serviceName -> (instanceId -> RegisteredInstance)
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, RegisteredInstance>> registry = new ConcurrentHashMap<>();

    public void register(InstanceDto dto) {
        RegisteredInstance instance = new RegisteredInstance(
                dto.getServiceName(),
                dto.getInstanceId(),
                dto.getUrl(),
                dto.getTtlSeconds(),
                dto.getMetadata()
        );

        ConcurrentHashMap<String, RegisteredInstance> instances =
                registry.get(dto.getServiceName());

        if (instances == null) {
            instances = new ConcurrentHashMap<>();
            registry.put(dto.getServiceName(), instances);
        }

        instances.put(dto.getInstanceId(), instance);
    }

    public void heartbeat(String instanceId) {
        registry.forEach((service, instances) -> {
            RegisteredInstance inst = instances.get(instanceId);
            if (inst != null) inst.updateHeartbeat();
        });
    }

    public void deregister(String instanceId) {
        registry.forEach((service, instances) -> instances.remove(instanceId));
    }

    public List<RegisteredInstance> getInstances(String serviceName) {
        Map<String, RegisteredInstance> instances = registry.get(serviceName);
        if (instances == null) return List.of();

        Instant now = Instant.now();
        List<RegisteredInstance> alive = new ArrayList<>();
        for (RegisteredInstance inst : instances.values()) {
            if (now.minusSeconds(inst.getTtlSeconds()).isBefore(inst.getLastSeen())) {
                alive.add(inst);
            }
        }
        return alive;
    }

    public void cleanupStale() {
        Instant now = Instant.now();
        registry.forEach((service, instances) -> {
            instances.values().removeIf(inst ->
                    now.minusSeconds(inst.getTtlSeconds()).isAfter(inst.getLastSeen()));
        });
    }

    public ConcurrentHashMap<String, ConcurrentHashMap<String, RegisteredInstance>> getAll() {
        return registry;
    }
}
