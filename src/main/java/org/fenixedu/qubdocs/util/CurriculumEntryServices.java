package org.fenixedu.qubdocs.util;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curricularPeriod.CurricularPeriod;
import org.fenixedu.academic.domain.student.curriculum.ICurriculum;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.fenixedu.qubdocs.academic.documentRequests.providers.ICreditsTransferRemarksCollection;

public interface CurriculumEntryServices {

    public CurricularPeriod getCurricularPeriod(final DegreeCurricularPlan dcp, final int year, final Integer semester);

    public CurricularPeriod getCurricularPeriod(final DegreeCurricularPlan dcp, final int year);

    public int getCurricularYear(final CurriculumLine input);

    public Map<CurricularPeriod, BigDecimal> mapYearCredits(final ICurriculum curriculum);

    public void mapYearCreditsLogger(final Map<CurricularPeriod, BigDecimal> input);

    public void addYearCredits(final Map<CurricularPeriod, BigDecimal> result, final CurricularPeriod curricularPeriod,
            final BigDecimal credits, final String code);

    public int getCurricularSemester(final CurriculumLine curriculumLine);

    public ICreditsTransferRemarksCollection buildRemarksFor(Collection<ICurriculumEntry> entries,
            StudentCurricularPlan curricularPlan);

}
