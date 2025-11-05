package ru.astondevs.discoveryserviceaston.model;

import java.time.Instant;
import java.util.Map;

public class RegisteredInstance {
    private final String serviceName;
    private final String instanceId;
    private final String url;
    private final long ttlSeconds;
    private final Map<String, String> metadata;
    private Instant lastSeen;

    public RegisteredInstance(String serviceName, String instanceId, String url,
                              long ttlSeconds, Map<String, String> metadata) {
        this.serviceName = serviceName;
        this.instanceId = instanceId;
        this.url = url;
        this.ttlSeconds = ttlSeconds;
        this.metadata = metadata;
        this.lastSeen = Instant.now();
    }

    public void updateHeartbeat() {
        this.lastSeen = Instant.now();
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getUrl() {
        return url;
    }

    public long getTtlSeconds() {
        return ttlSeconds;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public Instant getLastSeen() {
        return lastSeen;
    }
}
