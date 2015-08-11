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

package org.fenixedu.qubdocs.ui.manage;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.GradeScale;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.EnrolmentEvaluationState;
import org.fenixedu.bennu.TupleDataSourceBean;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.qubdocs.ui.FenixeduQubdocsReportsBaseController;
import org.fenixedu.qubdocs.ui.FenixeduQubdocsReportsController;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.ist.fenixframework.Atomic;

import com.google.common.collect.Sets;

import edu.emory.mathcs.backport.java.util.Collections;

//@SpringFunctionality(app = DomainController.class, title = "label.title.manage")
@BennuSpringController(value = FenixeduQubdocsReportsController.class)
@RequestMapping("/looseevaluation")
public class LooseEvaluationBeanController extends FenixeduQubdocsReportsBaseController {

    private static final Comparator<EvaluationSeason> COMPARE_EVALUATION_SEASON_BY_NAME = (a, b) ->
    {
        int c = a.getName().getContent().compareTo(b.getName().getContent());

        return c != 0 ? c : a.getExternalId().compareTo(b.getExternalId());
    };

    @Autowired
    private HttpSession session;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/create/{scpId}/{executionSemesterId}", method = RequestMethod.GET)
    public String create(@PathVariable("scpId") final StudentCurricularPlan studentCurricularPlan,
            @PathVariable("executionSemesterId") final ExecutionSemester executionSemester, final Model model) {
        model.addAttribute("studentCurricularPlan", studentCurricularPlan);
        model.addAttribute(
                "LooseEvaluationBean_enrolment_options",
                studentCurricularPlan.getEnrolmentsSet().stream().filter(e -> e.getExecutionPeriod() == executionSemester)
                        .sorted((x, y) -> x.getName().getContent().compareTo(y.getName().getContent()))
                        .collect(Collectors.toList()));
        model.addAttribute("typeValues",
                EvaluationSeason.all().sorted(COMPARE_EVALUATION_SEASON_BY_NAME).collect(Collectors.toList()));
        model.addAttribute(
                "gradeScaleValues",
                Arrays.<GradeScale> asList(GradeScale.values()).stream()
                        .map(l -> new TupleDataSourceBean(((GradeScale) l).name(), ((GradeScale) l).getDescription()))
                        .collect(Collectors.<TupleDataSourceBean> toList()));

        model.addAttribute(
                "improvementSemesterValues",
                ExecutionSemester.readNotClosedPublicExecutionPeriods().stream()
                        .sorted(Collections.reverseOrder(ExecutionSemester.COMPARATOR_BY_BEGIN_DATE))
                        .collect(Collectors.toList()));

        model.addAttribute("executionSemester", executionSemester);

        final String url =
                String.format("/academicAdministration/studentEnrolments.do?scpID=%s&method=prepare",
                        studentCurricularPlan.getExternalId());

        String backUrl = GenericChecksumRewriter.injectChecksumInUrl(request.getContextPath(), url, session);
        model.addAttribute("backUrl", backUrl);

        final List<EnrolmentEvaluation> evaluations =
                studentCurricularPlan.getEnrolmentsSet().stream().filter(e -> e.getExecutionPeriod() == executionSemester)
                        .map(l -> l.getEvaluationsSet()).reduce((a, c) -> Sets.union(a, c)).orElse(Sets.newHashSet()).stream()
                        .filter(l -> l.getMarkSheet() == null).collect(Collectors.toList());

        model.addAttribute("evaluationsSet", evaluations);

        return "fenixedu-qubdocs-reports/manage/looseevaluationbean/create";
    }

    @RequestMapping(value = "/create/{scpId}/{executionSemesterId}", method = RequestMethod.POST)
    public String create(
            @PathVariable("scpId") final StudentCurricularPlan studentCurricularPlan,
            @PathVariable("executionSemesterId") final ExecutionSemester executionSemester,
            @RequestParam(value = "enrolment", required = false) Enrolment enrolment,
            @RequestParam(value = "availabledate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate availableDate,
            @RequestParam(value = "examdate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate examDate,
            @RequestParam(value = "gradescale", required = false) GradeScale gradeScale, @RequestParam(value = "grade",
                    required = false) String grade, @RequestParam(value = "type", required = false) EvaluationSeason type,
            @RequestParam(value = "improvementsemester", required = false) ExecutionSemester improvementSemester, Model model,
            final RedirectAttributes redirectAttributes) {

        try {

            if (!checkIfAllGradesAreSameScale(enrolment, gradeScale)) {
                addErrorMessage(BundleUtil.getString("resources.FenixeduQubdocsReportsResources",
                        "error.LooseEvaluationBean.grade.not.same.scale"), model);
                return create(studentCurricularPlan, executionSemester, model);
            }

            createLooseEvaluation(enrolment, examDate, Grade.createGrade(grade, gradeScale), type, improvementSemester);
            return redirect(
                    "/looseevaluation/create/" + studentCurricularPlan.getExternalId() + "/" + executionSemester.getExternalId(),
                    model, redirectAttributes);
        } catch (final DomainException e) {
            addErrorMessage(e.getLocalizedMessage(), model);
            return create(studentCurricularPlan, executionSemester, model);
        }
    }

    private boolean checkIfAllGradesAreSameScale(Enrolment enrolment, GradeScale gradeScale) {
        boolean result = true;
        for (final EnrolmentEvaluation enrolmentEvaluation : enrolment.getEvaluationsSet()) {
            result &= enrolmentEvaluation.getGradeScale() == gradeScale;
        }

        return result;
    }

    @Atomic
    public void createLooseEvaluation(Enrolment enrolment, LocalDate examDate, Grade grade, EvaluationSeason type,
            ExecutionSemester improvementSemester) {

        final EnrolmentEvaluation evaluation = new EnrolmentEvaluation(enrolment, type);
        if (type.isImprovement()) {
            evaluation.setExecutionPeriod(improvementSemester);
        }

        evaluation.edit(Authenticate.getUser().getPerson(), grade, new Date(), examDate.toDateTimeAtStartOfDay().toDate());
        evaluation.confirmSubmission(Authenticate.getUser().getPerson(), "");
    }

    @RequestMapping(value = "/delete/{scpId}/{evaluationId}/{executionSemesterId}", method = RequestMethod.POST)
    public String delete(@PathVariable("scpId") final StudentCurricularPlan studentCurricularPlan,
            @PathVariable("evaluationId") EnrolmentEvaluation enrolmentEvaluation,
            @PathVariable("executionSemesterId") final ExecutionSemester executionSemester, Model model,
            final RedirectAttributes redirectAttributes) {

        try {
            deleteEnrolment(enrolmentEvaluation);
        } catch (final DomainException e) {
            addErrorMessage(e.getLocalizedMessage(), model);
        }

        return redirect(
                "/looseevaluation/create/" + studentCurricularPlan.getExternalId() + "/" + executionSemester.getExternalId(),
                model, redirectAttributes);
    }

    @Atomic
    private void deleteEnrolment(EnrolmentEvaluation enrolmentEvaluation) {
        enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
        enrolmentEvaluation.delete();
    }
}
