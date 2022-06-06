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
import java.util.Comparator;
import java.util.Locale;
import java.util.Set;

import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.IEnrolment;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.fenixedu.academic.domain.studentCurriculum.Dismissal;
import org.fenixedu.academic.domain.studentCurriculum.ExternalEnrolment;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.qubdocs.util.CurriculumEntryServices;
import org.fenixedu.qubdocs.util.DocsStringUtils;
import org.joda.time.LocalDate;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;

public class CurriculumEntry implements Comparable<CurriculumEntry> {

    private final ICurriculumEntry iCurriculumEntry;
    private final CurriculumEntryRemarksDataProvider remarksDataProvider;
    private final Registration registration;
    private final CurriculumEntryServices service;

    public CurriculumEntry(final Registration registration, final ICurriculumEntry entry,
            final CurriculumEntryRemarksDataProvider remarksDataProvider, final CurriculumEntryServices service) {
        this.registration = registration;
        this.iCurriculumEntry = entry;
        this.remarksDataProvider = remarksDataProvider;
        this.service = service;
    }

    public LocalizedString getName() {
        if (iCurriculumEntry instanceof IEnrolment && ((IEnrolment) iCurriculumEntry).isEnrolment()) {
            return ((Enrolment) iCurriculumEntry).getCurricularCourse().getNameI18N(iCurriculumEntry.getExecutionPeriod());
        }

        return iCurriculumEntry.getName();
    }

    public BigDecimal getEctsCredits() {
        return iCurriculumEntry.getEctsCreditsForCurriculum();
    }

    public BigDecimal getWeight() {
        return iCurriculumEntry.getWeigthForCurriculum();
    }

    public BigDecimal getTheoreticalHours() {
        if (isCurriculumLine()) {
            return new BigDecimal(getCompetenceCourse().getTheoreticalHours(iCurriculumEntry.getExecutionPeriod()));
        }

        return null;
    }

    public BigDecimal getProblemHours() {
        if (isCurriculumLine()) {
            return new BigDecimal(getCompetenceCourse().getProblemsHours(iCurriculumEntry.getExecutionPeriod()));
        }

        return null;
    }

    public BigDecimal getLaboratorialHours() {
        if (isCurriculumLine()) {
            return new BigDecimal(getCompetenceCourse().getLaboratorialHours(iCurriculumEntry.getExecutionPeriod()));
        }

        return null;
    }

    public BigDecimal getContactLoadHours() {
        if (isCurriculumLine()) {
            return new BigDecimal(getCompetenceCourse().getContactLoad(iCurriculumEntry.getExecutionPeriod()));
        }

        return null;
    }

    public BigDecimal getSeminaryHours() {
        if (isCurriculumLine()) {
            return new BigDecimal(getCompetenceCourse().getSeminaryHours(iCurriculumEntry.getExecutionPeriod()));
        }

        return null;
    }

    public BigDecimal getFieldWorkHours() {
        if (isCurriculumLine()) {
            return new BigDecimal(getCompetenceCourse().getFieldWorkHours(iCurriculumEntry.getExecutionPeriod()));
        }

        return null;
    }

    public BigDecimal getTrainingPeriodHours() {
        if (isCurriculumLine()) {
            return new BigDecimal(getCompetenceCourse().getTrainingPeriodHours(iCurriculumEntry.getExecutionPeriod()));
        }

        return null;
    }

    public BigDecimal getTutorialOrientationHours() {
        if (isCurriculumLine()) {
            return new BigDecimal(getCompetenceCourse().getTutorialOrientationHours(iCurriculumEntry.getExecutionPeriod()));
        }

        return null;
    }

    public BigDecimal getOtherHours() {
        if (isCurriculumLine()) {
            double autonomousWorkHours = getCompetenceCourse().getAutonomousWorkHours(iCurriculumEntry.getExecutionPeriod());
            double fieldWorkHours = getCompetenceCourse().getFieldWorkHours(iCurriculumEntry.getExecutionPeriod());
            double seminaryHours = getCompetenceCourse().getSeminaryHours(iCurriculumEntry.getExecutionPeriod());
            return new BigDecimal(autonomousWorkHours + fieldWorkHours + seminaryHours);
        }

        return null;
    }

    public BigDecimal getTotalHours() {
        if (isCurriculumLine()) {
            double autonomousWorkHours = getCompetenceCourse().getAutonomousWorkHours(iCurriculumEntry.getExecutionPeriod());
            double fieldWorkHours = getCompetenceCourse().getFieldWorkHours(iCurriculumEntry.getExecutionPeriod());
            double seminaryHours = getCompetenceCourse().getSeminaryHours(iCurriculumEntry.getExecutionPeriod());
            double theoreticalHours = getCompetenceCourse().getTheoreticalHours(iCurriculumEntry.getExecutionPeriod());
            double problemsHours = getCompetenceCourse().getProblemsHours(iCurriculumEntry.getExecutionPeriod());
            double laboratorialHours = getCompetenceCourse().getLaboratorialHours(iCurriculumEntry.getExecutionPeriod());
            double trainingPeriodHours = getCompetenceCourse().getTrainingPeriodHours(iCurriculumEntry.getExecutionPeriod());
            double tutorialOrientationHours =
                    getCompetenceCourse().getTutorialOrientationHours(iCurriculumEntry.getExecutionPeriod());

            return new BigDecimal(autonomousWorkHours + fieldWorkHours + seminaryHours + theoreticalHours + problemsHours
                    + laboratorialHours + trainingPeriodHours + tutorialOrientationHours);
        }

        return null;
    }

    public String getCode() {
        return getCompetenceCourse().getCode();
    }

    public ExecutionInterval getExecutionSemester() {
        return iCurriculumEntry.getExecutionInterval();
    }

    public String getCurricularYear() {
        //TODO: algoritmo não funciona para substituições
        if (isExternal()) {
            return "-";
        }
        CurriculumLine input = (CurriculumLine) iCurriculumEntry;
        if (input.getCurriculumGroup().getDegreeModule() == null) {
            return "-";
        }

        return "" + service.getCurricularYear(input);
    }

    public String getGrade() {
        return iCurriculumEntry.getGradeValue();
    }

    public LocalizedString getGradeDescription() {
        return DocsStringUtils
                .capitalize(BundleUtil.getLocalizedString("resources.EnumerationResources", iCurriculumEntry.getGradeValue()));
    }

    public ExecutionYear getExecutionYear() {
        return iCurriculumEntry.getExecutionYear();
    }

    public String getExecutionYearName() {
        return getExecutionYear().getName();
    }

    public LocalDate getApprovementDate() {

        return getICurriculumEntry().getApprovementDate().toLocalDate();

    }

    protected boolean isCurriculumLine() {
        return iCurriculumEntry instanceof CurriculumLine;
    }

    protected CurriculumLine getCurriculumLine() {
        return (CurriculumLine) iCurriculumEntry;
    }

    protected Dismissal getDismissal() {
        return (Dismissal) iCurriculumEntry;
    }

    public IEnrolment getIEnrolment() {
        return (IEnrolment) iCurriculumEntry;
    }

    public ExternalEnrolment getExternalEnrolment() {
        return (ExternalEnrolment) iCurriculumEntry;
    }

    protected CompetenceCourse getCompetenceCourse() {
        return getCurriculumLine().getCurricularCourse().getCompetenceCourse();
    }

    public ICurriculumEntry getICurriculumEntry() {
        return iCurriculumEntry;
    }

    public boolean isExternal() {
        return iCurriculumEntry instanceof ExternalEnrolment;
    }

    public boolean isDismissal() {
        return iCurriculumEntry instanceof Dismissal;
    }

    public boolean isIEnrolment() {
        return iCurriculumEntry instanceof IEnrolment;
    }

    public boolean isExtraCurricular() {
        return iCurriculumEntry instanceof Enrolment && ((Enrolment) iCurriculumEntry).isExtraCurricular();
    }

    public boolean isPropedeutics() {
        return iCurriculumEntry instanceof Enrolment && ((Enrolment) iCurriculumEntry).isPropaedeutic();
    }

    public boolean isStandalone() {
        return iCurriculumEntry instanceof Enrolment && ((Enrolment) iCurriculumEntry).isStandalone();
    }

    public boolean isAnnual() {

        if (!(iCurriculumEntry instanceof CurriculumLine)) {
            return false;
        }

        final CurriculumLine line = (CurriculumLine) iCurriculumEntry;
        return line.getDegreeModule() != null && line.getCurricularCourse().getCompetenceCourse() != null
                && line.getCurricularCourse().getCompetenceCourse().isAnual();
    }

    public String getRemarkNumbers() {
        return remarksDataProvider.getRemarkIdsFor(this);
    }

    @Override
    public boolean equals(final Object obj) {
        return obj != null && obj instanceof CurriculumEntry
                && getICurriculumEntry() == ((CurriculumEntry) obj).getICurriculumEntry();
    }

    // TODO
    public LocalizedString getTypeOfCourse() {
//        if (isExternal()) {
//            return BundleUtil.getLocalizedString("resources.EnumerationResources", getExternalEnrolment().getEnrolmentTypeName(getStudentCurricularPlan()));
//
//        }
//
//        return QubBundleUtil.getLocalizedStringFromResourceBundle("resources.EnumerationResources",
//                PropertiesManager.getLocales(), getCurriculumLine().getEnrolmentTypeName(getStudentCurricularPlan()));

        return null;
    }

    protected StudentCurricularPlan getStudentCurricularPlan() {
        return registration.getLastStudentCurricularPlan();
    }

    private static Function<ICurriculumEntry, String> courseEctsGradeProvider = entry -> "";

    public static void setCourseEctsGradeProviderProvider(final Function<ICurriculumEntry, String> courseEctsGradeProvider) {
        CurriculumEntry.courseEctsGradeProvider = courseEctsGradeProvider;
    }

    protected String getEctsGrade(final ICurriculumEntry entry) {
        return courseEctsGradeProvider.apply(entry);
    }

    public String getEctsGrade() {
        return getEctsGrade(getICurriculumEntry());
    }

    public LocalizedString getDurationName() {
        if (isDismissal()) {
            return BundleUtil.getLocalizedString("resources.AcademicAdminOffice",
                    getDismissal().isAnual() ? "diploma.supplement.annual" : "diploma.supplement.semestral");
        }

        return BundleUtil.getLocalizedString("resources.AcademicAdminOffice",
                getIEnrolment().isAnual() ? "diploma.supplement.annual" : "diploma.supplement.semestral");
    }

    public static Set<CurriculumEntry> transform(final Registration registration,
            final Collection<? extends ICurriculumEntry> entries, final CurriculumEntryRemarksDataProvider remarksDataProvider,
            final CurriculumEntryServices service) {
        Set<CurriculumEntry> result = Sets.newHashSet();

        result.addAll(Collections2.transform(entries, new Function<ICurriculumEntry, CurriculumEntry>() {
            @Override
            public CurriculumEntry apply(final ICurriculumEntry entry) {
                return new CurriculumEntry(registration, entry, remarksDataProvider, service);
            }
        }));

        return result;
    }

    public static Comparator<CurriculumEntry> NAME_COMPARATOR(final Locale locale) {
        return new Comparator<CurriculumEntry>() {

            @Override
            public int compare(final CurriculumEntry o1, final CurriculumEntry o2) {
                String nameO1 =
                        o1.getName().getContent(locale) != null ? o1.getName().getContent(locale) : o1.getName().getContent();
                String nameO2 =
                        o2.getName().getContent(locale) != null ? o2.getName().getContent(locale) : o2.getName().getContent();

                int result = nameO1.compareTo(nameO2);

                if (result != 0) {
                    return result;
                }

                return o1.compareTo(o2);
            }

        };
    }

    public static Comparator<CurriculumEntry> EXECUTION_YEAR_NAME_COMPARATOR(final Locale locale) {
        final Comparator<CurriculumEntry> name_comparator = NAME_COMPARATOR(locale);
        return new Comparator<CurriculumEntry>() {

            @Override
            public int compare(final CurriculumEntry o1, final CurriculumEntry o2) {
                int result = o1.getExecutionYear().compareTo(o2.getExecutionYear());

                if (result != 0) {
                    return result;
                }

                return name_comparator.compare(o1, o2);
            }

        };
    }

    @Override
    public int compareTo(final CurriculumEntry o) {
        return ICurriculumEntry.COMPARATOR_BY_EXECUTION_PERIOD_AND_ID.compare(getICurriculumEntry(), o.getICurriculumEntry());
    }

}
