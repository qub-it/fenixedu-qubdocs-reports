package org.fenixedu.qubdocs.util.reports.helpers;

import java.util.Arrays;
import java.util.List;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;

import com.qubit.terra.docs.util.helpers.IDocumentHelper;

public class CurricularHelper implements IDocumentHelper {

    private static final List<CycleType> ACCEPTED_CYCLES =
            Arrays.asList(CycleType.FIRST_CYCLE, CycleType.SECOND_CYCLE, CycleType.THIRD_CYCLE);

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

                .filter(c -> !c.getParentContextsByExecutionYear(curriculumEntry.getExecutionYear()).isEmpty())

                .flatMap(cc -> cc.getParentCycleCourseGroups().stream())

                .map(c -> c.getCycleType())

                .filter(c -> ACCEPTED_CYCLES.contains(c))

                .sorted(CycleType.COMPARATOR_BY_GREATER_WEIGHT).findFirst().orElse(null);

    }

}
