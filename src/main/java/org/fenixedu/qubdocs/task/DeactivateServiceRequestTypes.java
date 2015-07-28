package org.fenixedu.qubdocs.task;

import org.fenixedu.academic.domain.serviceRequests.ServiceRequestType;
import org.fenixedu.bennu.scheduler.custom.CustomTask;

public class DeactivateServiceRequestTypes extends CustomTask {

    @Override
    public void runTask() throws Exception {
        ServiceRequestType.findAll().forEach(srt -> srt.setActive(false));
    }

}
