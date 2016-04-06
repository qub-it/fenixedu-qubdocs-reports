package org.fenixedu.qubdocs.util.reports.helpers;

import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.degreeStructure.CycleCourseGroup;
import org.fenixedu.academic.domain.degreeStructure.CycleType;

import com.qubit.terra.docs.util.helpers.IDocumentHelper;

public class CurricularHelper implements IDocumentHelper {

    private boolean isNormalEnrolment(Enrolment enrolment) {
        return enrolment.isAnnulled()
                && !enrolment.isStandalone()
                && !enrolment.isExtraCurricular()
                && !enrolment.isPropaedeutic()
                && (enrolment.getCurriculumGroup().isInternalCreditsSourceGroup() || !enrolment.getCurriculumGroup()
                        .isNoCourseGroupCurriculumGroup())
                && (enrolment.getParentCycleCurriculumGroup() == null || !enrolment.getParentCycleCurriculumGroup().isExternal());
    }

    public CycleType inferCourseHigherCycle(Enrolment enrolment, boolean competenceScope) {
        Set<CycleCourseGroup> sample = new HashSet<CycleCourseGroup>();
        CycleType result = null;
        if (isNormalEnrolment(enrolment)) {
            return enrolment.getParentCycleCurriculumGroup().getCycleType();
        }
        if (competenceScope) {
            for (CurricularCourse course : enrolment.getCurricularCourse().getCompetenceCourse()
                    .getAssociatedCurricularCoursesSet()) {
                sample.addAll(course.getParentCycleCourseGroups());
            }
        } else {
            sample.addAll(enrolment.getCurricularCourse().getParentCycleCourseGroups());
        }
        for (CycleCourseGroup group : sample) {
            if (result == null) {
                result = group.getCycleType();
            } else if (result.isBeforeOrEquals(group.getCycleType())) {
                result = group.getCycleType();
            }
        }
        return result;
    }

    public CycleType inferCourseLowerCycle(Enrolment enrolment, boolean competenceScope) {
        Set<CycleCourseGroup> sample = new HashSet<CycleCourseGroup>();
        CycleType result = null;
        if (isNormalEnrolment(enrolment)) {
            return enrolment.getParentCycleCurriculumGroup().getCycleType();
        }
        if (competenceScope) {
            for (CurricularCourse course : enrolment.getCurricularCourse().getCompetenceCourse()
                    .getAssociatedCurricularCoursesSet()) {
                sample.addAll(course.getParentCycleCourseGroups());
            }
        } else {
            sample.addAll(enrolment.getCurricularCourse().getParentCycleCourseGroups());
        }
        for (CycleCourseGroup group : sample) {
            if (result == null) {
                result = group.getCycleType();
            } else if (group.getCycleType().isBeforeOrEquals(result)) {
                result = group.getCycleType();
            }
        }
        return result;
    }

}
