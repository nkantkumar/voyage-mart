package com.voyage.booking.controller;

import com.voyage.shared.model.BookingRequest;
import com.voyage.shared.model.BookingResponse;
import com.voyage.shared.workflow.CruiseBookingWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    private final WorkflowClient workflowClient;

    public BookingController(WorkflowClient workflowClient) {
        this.workflowClient = workflowClient;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest request) {
        logger.info("Received booking request: {}", request);

        String workflowId = "booking-workflow-" + UUID.randomUUID().toString().substring(0, 8);

        CruiseBookingWorkflow workflow = workflowClient.newWorkflowStub(
                CruiseBookingWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setWorkflowId(workflowId)
                        .setTaskQueue("BookingQueue")
                        .build()
        );

        logger.info("Starting Temporal workflow {} for task queue BookingQueue", workflowId);
        
        BookingResponse response = workflow.bookCruise(request);
        
        logger.info("Workflow result: {}", response);
        return ResponseEntity.ok(response);
    }
}
