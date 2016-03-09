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
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.Dismissal;
import org.fenixedu.academic.dto.student.RegistrationConclusionBean;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.qubdocs.util.DocsStringUtils;
import org.joda.time.LocalDate;

import com.google.common.base.Function;
import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IReportDataProvider;

public class ConclusionInformationDataProvider implements IReportDataProvider {

    protected static final String KEY = "conclusionInformation";

    protected ConclusionInformation conclusionInformation;

    public ConclusionInformationDataProvider(final Registration registration, final ProgramConclusion programConclusion) {
        this.conclusionInformation = new ConclusionInformation(new RegistrationConclusionBean(registration, programConclusion));
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
            return this.conclusionInformation;
        }

        return null;
    }

    private static Function<RegistrationConclusionBean, String> degreeEctsGradeProvider = conclusion -> "";

    public static void setDegreeEctsGradeProviderProvider(Function<RegistrationConclusionBean, String> degreeEctsGradeProvider) {
        ConclusionInformationDataProvider.degreeEctsGradeProvider = degreeEctsGradeProvider;
    }

    public class ConclusionInformation {
        protected RegistrationConclusionBean conclusionBean;

        public ConclusionInformation(final RegistrationConclusionBean bean) {
            this.conclusionBean = bean;
        }

        public RegistrationConclusionBean getConclusionBean() {
            return conclusionBean;
        }

        public LocalDate getConclusionDate() {
            return conclusionBean.getConclusionDate().toLocalDate();
        }

        public String getAverage() {
            return conclusionBean.getRawGrade().getValue();
        }

        public String getFinalAverage() {
            return conclusionBean.getFinalGrade().getValue();
        }

        public String getRoundedFinalAverage() {
            BigDecimal average = new BigDecimal(getFinalAverage());
            return average.setScale(0, RoundingMode.HALF_EVEN).toString();
        }

        public String getEctsGrade() {
            return degreeEctsGradeProvider.apply(conclusionBean);
        }

        public LocalizedString getFinalAverageDescription() {
            return DocsStringUtils.capitalize(BundleUtil.getLocalizedString("resources.EnumerationResources",
                    getRoundedFinalAverage()));
        }

        public LocalizedString getQualitativeGrade() {
            if (conclusionBean.getDescriptiveGrade() != null && conclusionBean.getDescriptiveGrade().getExtendedValue() != null) {
                return conclusionBean.getDescriptiveGrade().getExtendedValue();
            }
            return new LocalizedString();
        }

        public BigDecimal getDismissalCredits() {

            final CurriculumGroup curriculumGroup = this.conclusionBean.getCurriculumGroup();
            final StudentCurricularPlan studentCurricularPlan = curriculumGroup.getStudentCurricularPlan();
            final List<Dismissal> dismissals =
                    studentCurricularPlan
                            .getDismissals()
                            .stream()
                            .filter(d -> d.getCredits().isCredits()
                                    && curriculumGroup.hasCurriculumModule(d.getCurriculumGroup())).collect(Collectors.toList());

            BigDecimal sum = BigDecimal.ZERO;
            for (Dismissal dismissal : dismissals) {
                sum = sum.add(dismissal.getEctsCreditsForCurriculum());
            }

            return sum;
        }

        public boolean isDismissalCreditsGiven() {
            return getDismissalCredits().compareTo(BigDecimal.ZERO) > 0;
        }
    }

}
