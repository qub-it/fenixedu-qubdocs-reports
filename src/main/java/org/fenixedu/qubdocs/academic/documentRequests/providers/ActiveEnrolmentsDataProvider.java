package org.fenixedu.qubdocs.academic.documentRequests.providers;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.qubdocs.academic.documentRequests.providers.EnrolmentsDataProvider.Enrolments;
import org.fenixedu.qubdocs.util.CurriculumEntryServices;

import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IReportDataProvider;

public class ActiveEnrolmentsDataProvider implements IReportDataProvider {
    protected static final String KEY = "enrolments";

    protected Registration registration;
    protected Locale locale;
    protected List<Enrolments> data;
    protected CurriculumEntryServices service;

    public ActiveEnrolmentsDataProvider(final Registration registration, final Set<ICurriculumEntry> enrolmentsInEnrolState,
            final Locale locale, final CurriculumEntryServices service) {
        this.registration = registration;
        this.locale = locale;
        this.service = service;
        data = new ArrayList<>();
        if (enrolmentsInEnrolState != null && !enrolmentsInEnrolState.isEmpty()) {
            data.add(new Enrolments("active", locale, registration, enrolmentsInEnrolState, service));
        }
    }

    @Override
    public void registerFieldsAndImages(final IDocumentFieldsData documentFieldsData) {
        for (Enrolments enrolments : data) {
            enrolments.registerCollections(documentFieldsData);
        }
    }

    @Override
    public boolean handleKey(final String key) {
        for (Enrolments enrolments : data) {
            if (enrolments.hasKey(key)) {
                return true;
            }
        }
        return KEY.equals(key);
    }

    @Override
    public Object valueForKey(final String key) {
        if (KEY.equals(key)) {
            return this;
        }
        for (Enrolments enrolments : data) {
            if (enrolments.hasKey(key)) {
                return enrolments.getValue(key);
            }
        }
        return null;
    }
}
