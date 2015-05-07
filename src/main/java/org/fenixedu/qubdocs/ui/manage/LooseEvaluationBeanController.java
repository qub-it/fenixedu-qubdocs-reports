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

import javax.servlet.http.HttpSession;

import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.GradeScale;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.qubdocs.ui.DomainBaseController;
import org.fenixedu.qubdocs.ui.FenixeduQubdocsReportsController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.ist.fenixframework.Atomic;

//@SpringFunctionality(app = DomainController.class, title = "label.title.manage")
@BennuSpringController(value = FenixeduQubdocsReportsController.class)
@RequestMapping("/looseevaluation")
public class LooseEvaluationBeanController extends DomainBaseController {

    @Autowired
    private HttpSession session;

    @RequestMapping(value = "/create/{scpId}", method = RequestMethod.GET)
    public String create(@PathVariable("scpId") final StudentCurricularPlan studentCurricularPlan, final Model model) {
        model.addAttribute("studentCurricularPlan", studentCurricularPlan);
        model.addAttribute("LooseEvaluationBean_enrolment_options", studentCurricularPlan.getEnrolmentsSet());
        model.addAttribute("typeValues", org.fenixedu.academic.domain.curriculum.EnrolmentEvaluationType.values());

        return "fenixedu-qubdocs-reports/manage/looseevaluationbean/create";
    }

    @RequestMapping(value = "/create/{scpId}", method = RequestMethod.POST)
    public RedirectView create(
            @PathVariable("scpId") final StudentCurricularPlan studentCurricularPlan,
            @RequestParam(value = "enrolment", required = false) org.fenixedu.academic.domain.Enrolment enrolment,
            @RequestParam(value = "availabledate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ") org.joda.time.LocalDate availableDate,
            @RequestParam(value = "examdate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ") org.joda.time.LocalDate examDate,
            @RequestParam(value = "grade", required = false) String grade,
            @RequestParam(value = "type", required = false) org.fenixedu.academic.domain.curriculum.EnrolmentEvaluationType type,
            Model model) {

        try {
            createLooseEvaluation(enrolment, availableDate, examDate, Grade.createGrade(grade, GradeScale.TYPE20), type);
        } catch (DomainException e) {
            e.printStackTrace();
            throw e;
        }

        final String url =
                String.format("/academicAdministration/studentEnrolments.do?scpID=%s&method=prepare",
                        studentCurricularPlan.getExternalId());

        String checksumUrl = GenericChecksumRewriter.injectChecksumInUrl(request.getContextPath(), url, session);
        return new RedirectView(checksumUrl, true);
    }

    @Atomic
    public void createLooseEvaluation(org.fenixedu.academic.domain.Enrolment enrolment, org.joda.time.LocalDate availableDate,
            org.joda.time.LocalDate examDate, org.fenixedu.academic.domain.Grade grade,
            org.fenixedu.academic.domain.curriculum.EnrolmentEvaluationType type) {

        final EnrolmentEvaluation evaluation = new EnrolmentEvaluation(enrolment, type);

        evaluation.edit(Authenticate.getUser().getPerson(), grade, availableDate.toDateTimeAtStartOfDay().toDate(), examDate
                .toDateTimeAtStartOfDay().toDate());

        evaluation.confirmSubmission(Authenticate.getUser().getPerson(), "");
    }
}
