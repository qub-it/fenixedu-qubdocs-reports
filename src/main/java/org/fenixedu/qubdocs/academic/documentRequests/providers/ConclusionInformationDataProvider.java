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
import java.util.List;
import java.util.Locale;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.EctsGraduationGradeConversionTable;
import org.fenixedu.academic.domain.degreeStructure.EctsTableIndex;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.studentCurriculum.Dismissal;
import org.fenixedu.academic.dto.student.RegistrationConclusionBean;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.qubdocs.util.DocsStringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IReportDataProvider;

public class ConclusionInformationDataProvider implements IReportDataProvider {

    protected static final String KEY = "conclusionInformation";

    protected Registration registration;
    protected CycleType cycleType;

    //protected IEctsConversionService service = EctsConversionServiceFactory.newInstance().getService();

    public ConclusionInformationDataProvider(final Registration registration, final CycleType cycleType) {
        this.registration = registration;
        this.cycleType = cycleType;
    }

    @Override
    public void registerFieldsAndImages(IDocumentFieldsData documentFieldsData) {

    }

    @Override
    public boolean handleKey(final String key) {
        return KEY.equals(key);
    }

    @Override
    public Object valueForKey(final String key) {
        if (handleKey(key)) {
            return conclusionInformation();
        }

        return null;
    }

    protected ConclusionInformation conclusionInformation() {
        return new ConclusionInformation(new RegistrationConclusionBean(registration, cycleType));
    }

    public class ConclusionInformation {
        protected RegistrationConclusionBean conclusionBean;

        public ConclusionInformation(final RegistrationConclusionBean bean) {

            this.conclusionBean = bean;
        }

        public LocalDate getConclusionDate() {
            return conclusionBean.getConclusionDate().toLocalDate();
        }

        public String getAverage() {
            return conclusionBean.getAverage().toPlainString();
        }

        public String getFinalAverage() {
            return conclusionBean.getFinalAverage().toString();
        }

        // TODO
        public LocalizedString getPhdFinalGrade() {
            //return GradeScaleType.findByGradeScale(GradeScale.TYPEAP).findValueByAcronym("APMB").getName();
            return null;
        }

        public LocalizedString getFinalAverageDescription() {
            return DocsStringUtils.capitalize(BundleUtil.getLocalizedString("resources.EnumerationResources", getFinalAverage()));
        }

        public LocalizedString getGraduateTitle() {
            LocalizedString mls = new LocalizedString();
            for (Locale locale : CoreConfiguration.supportedLocales()) {
                mls = mls.with(locale, registration.getGraduateTitle(cycleType, locale));
            }

            return mls;
        }

        // TODO
        public boolean isMobilityReferenceTable() {
//            EctsGraduationGradeConversionTable graduationGradeConversionTable = getGraduationEctsConversionTable();
//            return graduationGradeConversionTable.isMobilityReferenceTable();

            return false;
        }

        protected EctsGraduationGradeConversionTable getGraduationEctsConversionTable() {
            final ExecutionYear conclusionYear = conclusionBean.getConclusionYear();
            EctsGraduationGradeConversionTable graduationGradeConversionTable =
                    EctsTableIndex.getGraduationGradeConversionTable(registration.getDegree(), cycleType,
                            conclusionYear.getAcademicInterval(), new DateTime());
            return graduationGradeConversionTable;
        }

        // TODO
        public String convertGradeToEcts(final Integer value) {
//            Grade grade = Grade.createGrade(value.toString(), GradeScale.TYPE20);
//            final ExecutionYear conclusionYear = conclusionBean.getConclusionYear();
//            return service.convert(registration.getDegree(), cycleType, conclusionYear.getAcademicInterval(), grade).getValue();
            return null;
        }

        // TODO
        public BigDecimal getPercentageForGrade(final Integer value) {
//            Grade grade = Grade.createGrade(value.toString(), GradeScale.TYPE20);
//            final ExecutionYear conclusionYear = conclusionBean.getConclusionYear();
//
//            return service.percentage(registration.getDegree(), cycleType, conclusionYear.getAcademicInterval(), grade);
            return null;
        }

        // TODO
        public String getEctsGradeAverage() {
//            Integer finalAverage = conclusionBean.getFinalAverage();
//            Grade grade = Grade.createGrade(finalAverage.toString(), GradeScale.TYPE20);
//            final ExecutionYear conclusionYear = conclusionBean.getConclusionYear();
//
//            return service.convert(registration.getDegree(), cycleType, conclusionYear.getAcademicInterval(), grade).getValue();
            return null;
        }

        public LocalizedString getThesisTitle() {
//            final StudentCurricularPlan lastStudentCurricularPlan = registration.getLastStudentCurricularPlan();
//
//            if (lastStudentCurricularPlan == null) {
//                throw new DomainException("error.ConclusionInformationDataProvider.secondCycle.thesis.not.defined");
//            }
//
//            final Enrolment thesisEnrolment = lastStudentCurricularPlan.getLatestApprovedDissertationEnrolmentWithThesis();
//
//            if (thesisEnrolment == null) {
//                throw new DomainException("error.ConclusionInformationDataProvider.secondCycle.thesis.not.defined");
//            }
//
//            final Thesis thesis = thesisEnrolment.getThesis();
//
//            if (thesis == null) {
//                throw new DomainException("error.ConclusionInformationDataProvider.secondCycle.thesis.not.defined");
//            }
//
//            LocalizedString title = thesis.getTitle();
//            return title;
            return null;
        }

        public LocalizedString getPhdThesisTitle() {
            return new LocalizedString(Locale.ENGLISH, "Título da Tese de Doutoramento Teste");
        }

        public BigDecimal getDismissalCredits() {

            final List<Dismissal> dismissals = registration.getLastStudentCurricularPlan().getDismissals();

            BigDecimal sum = BigDecimal.ZERO;
            for (Dismissal dismissal : dismissals) {

                if (!dismissal.getCredits().isCredits()) {
                    continue;
                }

                sum = sum.add(dismissal.getEctsCreditsForCurriculum());
            }

            return sum;
        }

        public boolean isDismissalCreditsGiven() {
            return getDismissalCredits().compareTo(BigDecimal.ZERO) > 0;
        }
    }

}
