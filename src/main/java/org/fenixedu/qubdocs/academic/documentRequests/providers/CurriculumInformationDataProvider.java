package org.fenixedu.qubdocs.academic.documentRequests.providers;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.curriculum.Curriculum;
import org.joda.time.DateTime;

import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IReportDataProvider;

public class CurriculumInformationDataProvider implements IReportDataProvider {

    private static final String KEY = "curriculumInformation";

    private Registration registration;
    private ExecutionYear executionYear;

    public CurriculumInformationDataProvider(final Registration registration, final ExecutionYear executionYear) {
        this.registration = registration;
        this.executionYear = executionYear;
    }

    @Override
    public boolean handleKey(final String key) {
        return KEY.equals(key);
    }

    @Override
    public void registerFieldsAndImages(IDocumentFieldsData arg0) {
    }

    @Override
    public Object valueForKey(final String key) {
        if (handleKey(key)) {
            return this;
        }

        return null;
    }

    public Integer getPreviousCurricularYear() {
        final Curriculum curriculum =
                registration.getLastStudentCurricularPlan().getCurriculum(new DateTime(),
                        executionYear.getPreviousExecutionYear());

        return curriculum.getCurricularYear();
    }

    public Integer getCurricularYear() {
        final Curriculum curriculum = registration.getLastStudentCurricularPlan().getCurriculum(new DateTime(), executionYear);

        return curriculum.getCurricularYear();
    }

}
