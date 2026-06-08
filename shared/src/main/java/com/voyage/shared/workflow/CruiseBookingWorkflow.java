package com.voyage.shared.workflow;

import com.voyage.shared.model.BookingRequest;
import com.voyage.shared.model.BookingResponse;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface CruiseBookingWorkflow {
    
    @WorkflowMethod
    BookingResponse bookCruise(BookingRequest request);
}
