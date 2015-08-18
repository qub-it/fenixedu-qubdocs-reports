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

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Locale;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.DegreeOfficialPublication;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.qubdocs.util.DocsStringUtils;
import org.joda.time.LocalDate;

import com.google.common.collect.Sets;
import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IFieldsExporter;
import com.qubit.terra.docs.util.IReportDataProvider;

public class DegreeCurricularPlanInformationDataProvider implements IReportDataProvider {

    protected static final String KEY = "curricularPlanInformation";

    protected Registration registration;
    protected CycleType cycleType;
    protected ExecutionYear executionYear;
    protected LocalDate conclusionDate;

    public DegreeCurricularPlanInformationDataProvider(final Registration registration, final CycleType cycleType,
            final ExecutionYear executionYear) {
        this.registration = registration;
        this.cycleType = cycleType;
        this.executionYear = executionYear;
    }

    public DegreeCurricularPlanInformationDataProvider(final Registration registration, final ExecutionYear executionYear) {
        this(registration, null, executionYear);
    }

    public DegreeCurricularPlanInformationDataProvider(final Registration registration, final CycleType cycleType,
            final ExecutionYear executionYear, final LocalDate conclusionDate) {
        this(registration, cycleType, executionYear);
        this.conclusionDate = conclusionDate;

        checkData();
    }

    public DegreeCurricularPlanInformationDataProvider(final Registration registration, final ExecutionYear executionYear,
            final LocalDate conclusionDate) {
        this(registration, null, executionYear, conclusionDate);
    }

    protected void checkData() {
        if (getDegreeOfficialPublication() == null) {
            throw new DomainException("error.DegreeCurricularPlanInformationDataProvider.degreeOfficialPublication.required");
        }
    }

    @Override
    public void registerFieldsAndImages(final IDocumentFieldsData documentFieldsData) {

    }

    @Override
    public boolean handleKey(final String key) {
        return KEY.equals(key);
    }

    @Override
    public Object valueForKey(final String key) {
        return handleKey(key) ? this : null;
    }

    private DegreeCurricularPlan getDegreeCurricularPlan() {
        return getDegree().getDegreeCurricularPlansForYear(executionYear).iterator().next();
    }

    // TODO
    public LocalizedString getDegreeCertifiedName() {
//        DegreeType degreeType = getDegree().getDegreeType();
//        if (cycleType == null && (!degreeType.isBolonhaType() || degreeType.isComposite())) {
//            return getDegreeName();
//        }
//
//        final CycleType theCycleType = cycleType != null ? cycleType : getDegree().getCycleTypes().iterator().next();
//
//        final CycleCourseGroupInformation mostRecentCycleCourseGroupInformation =
//                getDegreeCurricularPlan().getCycleCourseGroup(theCycleType).getMostRecentCycleCourseGroupInformation(
//                        executionYear);
//        return mostRecentCycleCourseGroupInformation != null ? mostRecentCycleCourseGroupInformation.getGraduatedTitle() : getDegreeName();
        return null;
    }

    public LocalizedString getDegreeName() {
        return getDegree().getNameI18N().toLocalizedString();
    }

    public LocalizedString getDegreePresentationName() {
        return getDegree().getPresentationNameI18N(executionYear);
    }

    private Degree getDegree() {
        return registration.getDegree();
    }

    public LocalizedString getDegreeTypeFilteredName() {
        return registration.getDegreeType().getName();
    }

    // TODO
    public LocalizedString getBranchName() {
//        if (!isCurricularPlanWithBranch()) {
//            return null;
//        }
//
//        final CurriculumGroup branch = registration.getLastStudentCurricularPlan().getInternalCyclesBranches().iterator().next();
//        return branch.getDegreeModule().getNameI18N();
        return null;
    }

    // TODO
    public boolean isCurricularPlanWithBranch() {

//        if (registration.getLastStudentCurricularPlan().getInternalCyclesBranches().size() > 1) {
//            throw new DomainException("error.DegreeCurricularPlanInformationProvider.more.than.one.branch.found");
//        }
//
//        if (registration.getLastStudentCurricularPlan().getInternalCyclesBranches().isEmpty()) {
//            return false;
//        }
//
//        return true;
        return false;
    }

    // TODO
    public LocalizedString getCycleTypeName() {
        if (cycleType != null) {
            return cycleType.getDescriptionI18N();
        }

        if (registration.getDegreeType().getCycleTypes().size() == 1) {
            return null; // return registration.getDegreeType().getCycleTypes().iterator().next().getDescriptionI18N();
        }

        throw new DomainException("error.DegreeCurricularPlanInformationProvider.more.than.one.cycle");
    }

    public CycleType getCycleType() {
        return cycleType;
    }

    public Registration getRegistration() {
        return registration;
    }

    public Integer getNumberOfYears() {
        return 0;// registration.getDegreeType().getYears(getCycleType());
    }

    public Integer getNumberOfSemesters() {
        return 0;// registration.getDegreeType().getSemesters(getCycleType());
    }

    public BigDecimal getEcts() {
        return new BigDecimal(registration.getLastStudentCurricularPlan().getDegreeCurricularPlan()
                .getCycleCourseGroup(getCycleType()).getDefaultEcts(executionYear));
    }

    protected DegreeOfficialPublication getDegreeOfficialPublication() {
        return getDegree().getOfficialPublication(conclusionDate.toDateTimeAtStartOfDay());
    }

    public String getDegreeOfficialPublicationName() {
        if (getDegreeOfficialPublication() == null) {
            throw new DomainException("error.DegreeCurricularPlanInformationDataProvider.degreeOfficialPublication.empty");
        }

        return getDegreeOfficialPublication().getOfficialReference();
    }

    public LocalDate getDegreeOfficialPublicationDate() {
        if (getDegreeOfficialPublication() == null) {
            throw new DomainException("error.DegreeCurricularPlanInformationDataProvider.degreeOfficialPublication.empty");
        }

        return getDegreeOfficialPublication().getPublication();
    }

    // TODO
    public Collection<LocalizedString> getSpecializationAreaNames() {
//        return Collections2.transform(getDegreeOfficialPublication().getSpecializationArea(),
//                new Function<DegreeSpecializationArea, LocalizedString>() {
//                    public LocalizedString apply(final DegreeSpecializationArea specializationArea) {
//                        return DocsStringUtils.toLocalizedString(specializationArea.getName());
//                    }
//                });

        return Sets.newHashSet();
    }

    // TODO
    public LocalizedString getPrevailingScientificArea() {
        // return getDegree().getPrevailingScientificAreaI18N(executionYear);
        return null;
    }

    // TODO
    public LocalizedString getQualificationLevel() {
        // return getDegree().getQualificationLevel(executionYear);
        return null;
    }

    // TODO
    public LocalizedString getLearningLanguages() {
        // return getDegree().getMostRecentDegreeInfo(executionYear.getAcademicInterval()).getLearningLanguages();
        return null;
    }

    // TODO
    public LocalizedString getOfficialProgramDuration() {
//        return getDegree().getMostRecentDegreeInfo(executionYear.getAcademicInterval()).getOfficialProgramDuration();
        return null;
    }

    // TODO
    public LocalizedString getStudyRegime() {
//        return getDegree().getMostRecentDegreeInfo(executionYear.getAcademicInterval()).getStudyRegime();
        return null;
    }

    // TODO
    public LocalizedString getGraduationNextLevelAccess() {
//        return getDegree().getMostRecentDegreeInfo(executionYear.getAcademicInterval()).getGraduationNextLevelAccess();
        return null;
    }

    // TODO
    public LocalizedString getProfessionalStatute() {
//        return getDegree().getMostRecentDegreeInfo(executionYear.getAcademicInterval()).getProfessionalStatute();
        return null;
    }

    public boolean isSecondCycle() {
        return cycleType == CycleType.SECOND_CYCLE || getDegree().isSecondCycle();
    }

    public boolean isFirstCycle() {
        return cycleType == CycleType.FIRST_CYCLE || getDegree().isFirstCycle();
    }

    public boolean isThirdCycle() {
        return getDegree().isThirdCycle();
    }

    public boolean isFiveYearsDegree() {
        DegreeType degreeType = getDegree().getDegreeType();
        return !degreeType.isBolonhaType() && degreeType.isDegree();
    }

}
