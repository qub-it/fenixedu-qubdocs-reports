package org.fenixedu.qubdocs.util.reports.helpers;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CycleCourseGroup;
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

        Collection<Context> contexts =
                ((CurricularCourse) degreeModule).getCompetenceCourse().getAssociatedCurricularCoursesSet().stream()

                        .flatMap(cc -> cc.getParentContextsByExecutionYear(curriculumEntry.getExecutionYear()).stream())

                        .collect(Collectors.toSet());

        Collection<CycleCourseGroup> result = new HashSet<>();
        for (Context context : contexts) {
            if (context.getParentCourseGroup().isCycleCourseGroup()) {
                result.add((CycleCourseGroup) context.getParentCourseGroup());
            }
            result.addAll(context.getParentCourseGroup().getParentCycleCourseGroups());
        }

        return result.stream().map(ccg -> ccg.getCycleType()).filter(ct -> ACCEPTED_CYCLES.contains(ct))
                .sorted(CycleType.COMPARATOR_BY_GREATER_WEIGHT).findFirst().orElse(null);
    }

    public CycleType getCourseLowerCycle(final ICurriculumEntry curriculumEntry, final boolean competenceScope) {

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

        Collection<Context> contexts =
                ((CurricularCourse) degreeModule).getCompetenceCourse().getAssociatedCurricularCoursesSet().stream()

                        .flatMap(cc -> cc.getParentContextsByExecutionYear(curriculumEntry.getExecutionYear()).stream())

                        .collect(Collectors.toSet());

        Collection<CycleCourseGroup> result = new HashSet<>();
        for (Context context : contexts) {
            if (context.getParentCourseGroup().isCycleCourseGroup()) {
                result.add((CycleCourseGroup) context.getParentCourseGroup());
            }
            result.addAll(context.getParentCourseGroup().getParentCycleCourseGroups());
        }

        return result.stream().map(ccg -> ccg.getCycleType()).filter(ct -> ACCEPTED_CYCLES.contains(ct))
                .sorted(CycleType.COMPARATOR_BY_GREATER_WEIGHT.reversed()).findFirst().orElse(null);
    }

}
