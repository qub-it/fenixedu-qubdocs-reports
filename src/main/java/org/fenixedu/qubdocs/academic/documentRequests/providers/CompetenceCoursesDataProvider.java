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
import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;

import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IFieldsExporter;
import com.qubit.terra.docs.util.IReportDataProvider;

public class CompetenceCoursesDataProvider implements IReportDataProvider {

    private static final String KEY = "competenceCourses";

    Set<CompetenceCourseWrapper> competenceCourseWrapper = new HashSet<CompetenceCourseWrapper>();

    public CompetenceCoursesDataProvider(final Collection<? extends CurriculumLine> curriculumLines) {
        for (CurriculumLine curriculumLine : curriculumLines) {
            competenceCourseWrapper.add(new CompetenceCourseWrapper(curriculumLine.getCurricularCourse().getCompetenceCourse())
                    .withExecutionInterval(curriculumLine.getExecutionPeriod()));
        }
    }

    @Override
    public boolean handleKey(final String key) {
        return KEY.equals(key);
    }

    @Override
    public void registerFieldsAndImages(final IDocumentFieldsData documentFieldsData) {
        documentFieldsData.registerCollectionAsField(KEY);
    }

    @Override
    public Object valueForKey(final String key) {
        return handleKey(KEY) ? competenceCourseWrapper : null;
    }

    public static class CompetenceCourseWrapper {

        protected CompetenceCourse competenceCourse;
        protected ExecutionSemester executionSemester;

        public CompetenceCourseWrapper(final CompetenceCourse competenceCourse) {
            this.competenceCourse = competenceCourse;
        }

        public CompetenceCourseWrapper withExecutionInterval(final ExecutionSemester executionSemester) {
            this.executionSemester = executionSemester;
            return this;
        }

        public String getName() {
            return competenceCourse.getName();
        }

        public String getExecutionYear() {
            return executionSemester.getExecutionYear().getName();
        }

        public String getAutonomousWorkHours() {
            return new BigDecimal(this.competenceCourse.getAutonomousWorkHours(executionSemester)).toPlainString();
        }

        public String getTotalHours() {
            return new BigDecimal(this.competenceCourse.getTotalLoad(executionSemester)).toPlainString();
        }

        public String getContactHours() {
            return new BigDecimal(this.competenceCourse.getContactLoad(executionSemester)).toPlainString();
        }

    }

}
