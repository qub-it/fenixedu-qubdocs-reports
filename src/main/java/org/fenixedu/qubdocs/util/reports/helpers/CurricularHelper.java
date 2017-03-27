package org.fenixedu.qubdocs.util.reports.helpers;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;

import com.qubit.terra.docs.util.helpers.IDocumentHelper;

public class CurricularHelper implements IDocumentHelper {

    public CycleType getCourseHigherCycle(final ICurriculumEntry curriculumEntry, final boolean competenceScope) {

        if (!(curriculumEntry instanceof CurriculumLine)) {
            return null;
        }

        final CurriculumLine curriculumLine = (CurriculumLine) curriculumEntry;

        if (!competenceScope) {
            final CycleCurriculumGroup cycleCurriculumGroup = curriculumLine.getParentCycleCurriculumGroup();
            return cycleCurriculumGroup != null ? cycleCurriculumGroup.getCycleType() : null;
        }

        final DegreeModule degreeModule = curriculumLine.getDegreeModule();
        if (!(degreeModule instanceof CurricularCourse)) {
            return null;
        }

        return ((CurricularCourse) degreeModule).getCompetenceCourse().getAssociatedCurricularCoursesSet().stream()

                .flatMap(cc -> cc.getParentCycleCourseGroups().stream())

                .map(c -> c.getCycleType())

                .sorted(CycleType.COMPARATOR_BY_GREATER_WEIGHT).findFirst().orElse(null);

    }

}
