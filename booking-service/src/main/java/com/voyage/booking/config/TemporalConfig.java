package com.voyage.booking.config;

import com.voyage.booking.workflow.CruiseBookingWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemporalConfig implements SmartLifecycle {
    private static final Logger logger = LoggerFactory.getLogger(TemporalConfig.class);

    @Value("${temporal.address:localhost:7233}")
    private String temporalAddress;

    @Value("${temporal.queue:BookingQueue}")
    private String taskQueue;

    private WorkerFactory factory;
    private boolean running = false;

    @Bean
    public WorkflowServiceStubs workflowServiceStubs() {
        logger.info("Connecting to Temporal service stub at {}", temporalAddress);
        return WorkflowServiceStubs.newServiceStubs(
                WorkflowServiceStubsOptions.newBuilder()
                        .setTarget(temporalAddress)
                        .build()
        );
    }

    @Bean
    public WorkflowClient workflowClient(WorkflowServiceStubs serviceStubs) {
        return WorkflowClient.newInstance(serviceStubs);
    }

    @Bean
    public WorkerFactory workerFactory(WorkflowClient client) {
        this.factory = WorkerFactory.newInstance(client);
        Worker worker = factory.newWorker(taskQueue);
        worker.registerWorkflowImplementationTypes(CruiseBookingWorkflowImpl.class);
        logger.info("Registered CruiseBookingWorkflowImpl with task queue: {}", taskQueue);
        return factory;
    }

    @Override
    public void start() {
        logger.info("Starting Temporal Worker Factory...");
        if (factory != null) {
            factory.start();
            running = true;
            logger.info("Temporal Worker Factory started successfully.");
        }
    }

    @Override
    public void stop() {
        logger.info("Stopping Temporal Worker Factory...");
        if (factory != null) {
            factory.shutdown();
            running = false;
            logger.info("Temporal Worker Factory stopped.");
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
