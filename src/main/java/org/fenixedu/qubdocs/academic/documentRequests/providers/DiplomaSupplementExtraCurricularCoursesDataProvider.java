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

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.Dismissal;

import com.google.common.collect.Sets;

public class DiplomaSupplementExtraCurricularCoursesDataProvider extends ExtraCurricularCoursesDataProvider {

    public DiplomaSupplementExtraCurricularCoursesDataProvider(final Registration registration, final CycleType cycleType,
            final Locale locale, final CurriculumEntryRemarksDataProvider remarksDataProvider,
            final Boolean includeAllRegistrations, final Boolean includeSubstitutionCreditations) {
        super(registration, cycleType, locale, remarksDataProvider, includeAllRegistrations, includeSubstitutionCreditations);
    }

    @Override
    public Set<CurriculumEntry> getCurriculumEntries() {
        if (this.curriculumEntries == null) {
            this.curriculumEntries = Sets.newTreeSet(CurriculumEntry.NAME_COMPARATOR(locale));

            Collection<Registration> registrationList =
                    includeAllRegistrations ? registration.getStudent().getActiveRegistrations() : Collections
                            .singleton(registration);

            Set<CurriculumLine> extraCurricularCurriculumLines = Sets.newHashSet();

            for (Registration activeRegistration : registrationList) {
                extraCurricularCurriculumLines.addAll(activeRegistration.getExtraCurricularCurriculumLines());
            }
            
            if(cycleType == CycleType.FIRST_CYCLE) {
                // Check for second cycle curriculum group
                final CycleCurriculumGroup secondCycleCurriculumGroup = registration.getLastStudentCurricularPlan().getCycle(CycleType.SECOND_CYCLE);
                
                if(secondCycleCurriculumGroup != null) {
                    extraCurricularCurriculumLines.addAll(secondCycleCurriculumGroup.getAllCurriculumLines());
                }
            }

            for (CurriculumLine curriculumLine : extraCurricularCurriculumLines) {
                if(!curriculumLine.isApproved()) {
                    continue;
                }
                
                if (curriculumLine.isEnrolment() && ((Enrolment) curriculumLine).isSourceOfAnyCreditsInCurriculum()) {
                    continue;
                }

                if (curriculumLine.isDismissal() && !((Dismissal) curriculumLine).getCredits().isSubstitution()) {
                    continue;
                }

                if (curriculumLine.isDismissal() && !includeSubstitutionCreditations) {
                    continue;
                }

                curriculumEntries.addAll(CurriculumEntry.transform(registration, curriculumLine.getCurriculum()
                        .getCurriculumEntries(), remarksDataProvider));
            }
        }

        return this.curriculumEntries;
    }

}
