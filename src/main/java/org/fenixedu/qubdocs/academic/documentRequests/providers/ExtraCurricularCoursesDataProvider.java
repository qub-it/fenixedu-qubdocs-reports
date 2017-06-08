/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: anil.mamede@qub-it.com
 *
 *
 * This file is part of FenixEdu QubDocs.
 *
 * FenixEdu QubDocs is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu QubDocs is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu QubDocs.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.fenixedu.qubdocs.academic.documentRequests.providers;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.Dismissal;
import org.fenixedu.qubdocs.util.CurriculumEntryServices;

import com.google.common.collect.Sets;
import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IReportDataProvider;

public class ExtraCurricularCoursesDataProvider implements IReportDataProvider {

    protected static final String KEY = "extraCurricularCourses";
    protected static final String KEY_FOR_LIST = "extraCurricularCoursesList";

    protected CurriculumEntryRemarksDataProvider remarksDataProvider;
    protected Registration registration;
    protected CycleType cycleType;
    protected TreeSet<CurriculumEntry> curriculumEntries;
    protected Locale locale;
    protected Boolean includeAllRegistrations;
    protected Boolean includeSubstitutionCreditations;
    protected CurriculumEntryServices service;

    public ExtraCurricularCoursesDataProvider(final Registration registration, final CycleType cycleType, final Locale locale,
            final CurriculumEntryRemarksDataProvider remarksDataProvider, final Boolean includeAllRegistrations,
            final Boolean includeSubstitutionCreditations, final CurriculumEntryServices service) {
        this.registration = registration;
        this.cycleType = cycleType;
        this.remarksDataProvider = remarksDataProvider;
        this.locale = locale;
        this.includeAllRegistrations = includeAllRegistrations;
        this.includeSubstitutionCreditations = includeSubstitutionCreditations;
        this.service = service;
    }

    public ExtraCurricularCoursesDataProvider(final Collection<? extends ICurriculumEntry> entries, final Locale locale,
            final CurriculumEntryRemarksDataProvider remarks) {
        this.remarksDataProvider = remarks;
        this.locale = locale;
        this.includeAllRegistrations = false;
        this.includeSubstitutionCreditations = false;
        curriculumEntries = Sets.newTreeSet(CurriculumEntry.NAME_COMPARATOR(locale));
        curriculumEntries.addAll(CurriculumEntry.transform(registration, entries, this.remarksDataProvider, service));
    }

    @Override
    public void registerFieldsAndImages(final IDocumentFieldsData documentFieldsData) {
        documentFieldsData.registerCollectionAsField(KEY_FOR_LIST);
    }

    @Override
    public boolean handleKey(final String key) {
        return KEY.equals(key) || KEY_FOR_LIST.equals(key);
    }

    @Override
    public Object valueForKey(final String key) {
        if (KEY.equals(key)) {
            return this;
        } else if (KEY_FOR_LIST.equals(key)) {
            return this.getCurriculumEntries();
        }

        return null;
    }

    public Set<CurriculumEntry> getCurriculumEntries() {
        if (this.curriculumEntries == null) {
            this.curriculumEntries = Sets.newTreeSet(CurriculumEntry.NAME_COMPARATOR(locale));

            Collection<Registration> registrationList = includeAllRegistrations ? registration.getStudent()
                    .getActiveRegistrations() : Collections.singleton(registration);

            for (Registration activeRegistration : registrationList) {
                Collection<CurriculumLine> extraCurricularCurriculumLines =
                        activeRegistration.getExtraCurricularCurriculumLines();

                for (CurriculumLine curriculumLine : extraCurricularCurriculumLines) {
                    if (curriculumLine.isEnrolment() && ((Enrolment) curriculumLine).isSourceOfAnyCreditsInCurriculum()) {
                        continue;
                    }

                    if (curriculumLine.isDismissal() && !((Dismissal) curriculumLine).getCredits().isSubstitution()) {
                        continue;
                    }

                    if (curriculumLine.isDismissal() && !includeSubstitutionCreditations) {
                        continue;
                    }

                    curriculumEntries.addAll(CurriculumEntry.transform(registration,
                            curriculumLine.getCurriculum().getCurriculumEntries(), remarksDataProvider, service));
                }
            }

            if (registration.getDegree().isFirstCycle()
                    && registration.getLastStudentCurricularPlan().getCycle(CycleType.SECOND_CYCLE) != null) {
                CycleCurriculumGroup cycle = registration.getLastStudentCurricularPlan().getCycle(CycleType.SECOND_CYCLE);

                Collection<CurriculumLine> approvedCurriculumLines = cycle.getApprovedCurriculumLines();

                for (final CurriculumLine curriculumLine : approvedCurriculumLines) {
                    if (curriculumLine.isEnrolment() && !((Enrolment) curriculumLine).isSourceOfAnyCreditsInCurriculum()) {
                        curriculumEntries.addAll(CurriculumEntry.transform(registration,
                                curriculumLine.getCurriculum().getCurriculumEntries(), remarksDataProvider, service));
                    }
                }
            }

        }

        return this.curriculumEntries;
    }

    public boolean isEmpty() {
        return getCurriculumEntries().isEmpty();
    }

}
