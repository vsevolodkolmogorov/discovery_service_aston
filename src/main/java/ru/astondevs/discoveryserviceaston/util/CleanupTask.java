package ru.astondevs.discoveryserviceaston.util;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.astondevs.discoveryserviceaston.service.ServiceRegistry;

@Component
@EnableScheduling
public class CleanupTask {

    private final ServiceRegistry registry;

    public CleanupTask(ServiceRegistry registry) {
        this.registry = registry;
    }

    @Scheduled(fixedDelayString = "10000")
    public void cleanup() {
        registry.cleanupStale();
    }
}
