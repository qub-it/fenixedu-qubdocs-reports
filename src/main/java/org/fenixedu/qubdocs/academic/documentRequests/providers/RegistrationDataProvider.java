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

import java.util.Comparator;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.student.Registration;

import com.google.common.collect.Sets;
import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IReportDataProvider;

public class RegistrationDataProvider implements IReportDataProvider {

    protected static final String KEY = "registration";

    protected Registration registration;

    //TODOJN - temporary until Diogo returns
    protected static final String KEY_FOR_ENROLMENTS = "allEnrolments";
    private CurriculumEntryRemarksDataProvider remarksDataProvider;
    private Locale locale;
    protected Set<CurriculumEntry> allEnrolments;

    public RegistrationDataProvider(final Registration registration, Locale locale) {
        this.registration = registration;
        //TODOJN
        this.locale = locale;
        this.remarksDataProvider = new CurriculumEntryRemarksDataProvider(registration);
    }

    //TODOJN
    protected Set<CurriculumEntry> getAllEnrolments() {
        if (allEnrolments == null) {
            processEnrolments();
        }
        return allEnrolments;
    }

    //TODOJN
    private void processEnrolments() {
        Set<Enrolment> allEnrolmentsSet = new TreeSet<Enrolment>(Enrolment.COMPARATOR_BY_NAME_AND_ID);
        for (StudentCurricularPlan plan : registration.getStudentCurricularPlansSet()) {
            allEnrolmentsSet.addAll(plan.getAllEnrollments());
        }
        this.allEnrolments = Sets.newTreeSet(new Comparator<CurriculumEntry>() {

            @Override
            public int compare(final CurriculumEntry left, final CurriculumEntry right) {
                if (left.getExecutionYear() == right.getExecutionYear()) {
                    return compareByName(left, right);
                }
                return left.getExecutionYear().compareTo(right.getExecutionYear());
            }

            public int compareByName(final CurriculumEntry left, final CurriculumEntry right) {
                String leftContent = left.getName().getContent(locale) != null ? left.getName().getContent(locale) : left
                        .getName().getContent();
                String rightContent = right.getName().getContent(locale) != null ? right.getName().getContent(locale) : right
                        .getName().getContent();
                leftContent = leftContent.toLowerCase();
                rightContent = rightContent.toLowerCase();

                return leftContent.compareTo(rightContent);
            }
        });
        this.allEnrolments.addAll(CurriculumEntry.transform(registration, allEnrolmentsSet, remarksDataProvider));
    }

    @Override
    public boolean handleKey(final String key) {
        return KEY.equals(key) || KEY_FOR_ENROLMENTS.equals(key);
    }

    @Override
    public void registerFieldsAndImages(IDocumentFieldsData arg0) {

    }

    @Override
    public Object valueForKey(final String key) {
        if (KEY.equals(key)) {
            return registration;
        } else if (KEY_FOR_ENROLMENTS.equals(key)) {
            return getAllEnrolments();
        } else {
            return null;
        }
//        return handleKey(key) ? registration : null;
    }

}
