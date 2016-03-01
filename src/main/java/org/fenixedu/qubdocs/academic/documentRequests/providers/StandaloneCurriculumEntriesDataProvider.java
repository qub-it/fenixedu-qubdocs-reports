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

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.joda.time.LocalDate;

import com.google.common.collect.Sets;
import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IReportDataProvider;

public class StandaloneCurriculumEntriesDataProvider implements IReportDataProvider {

    protected static final String KEY = "standaloneCurriculumEntries";
    protected static final String KEY_FOR_LIST = "standaloneCurriculumEntriesList";

    protected Registration registration;
    protected CurriculumEntryRemarksDataProvider remarksDataProvider;
    protected Locale locale;
    protected Collection<Enrolment> sourceLines;
    protected TreeSet<CurriculumEntry> curriculumEntries;
    protected LocalDate emissionDate;

    public StandaloneCurriculumEntriesDataProvider(final Registration registration,
            final CurriculumEntryRemarksDataProvider remarksDataProvider, final Locale locale, final LocalDate emissionDate) {
        this.registration = registration;
        this.remarksDataProvider = remarksDataProvider;
        this.locale = locale;
        this.emissionDate = emissionDate;
    }

    public StandaloneCurriculumEntriesDataProvider(final Registration registration, final Collection<Enrolment> sourceLines,
            final CurriculumEntryRemarksDataProvider remarksDataProvider, final Locale locale, final LocalDate emissionDate) {
        this(registration, remarksDataProvider, locale, emissionDate);
        this.sourceLines = sourceLines;
    }

    @Override
    public void registerFieldsAndImages(IDocumentFieldsData documentFieldsData) {
        documentFieldsData.registerCollectionAsField(KEY_FOR_LIST);
    }

    @Override
    public boolean handleKey(final String key) {
        return KEY.equals(key) || KEY_FOR_LIST.equals(key);
    }

    @Override
    public Object valueForKey(final String key) {
        if (KEY.equals(key)) {
            return this;
        }

        if (KEY_FOR_LIST.equals(key)) {
            return getCurriculumEntries();
        }

        return null;
    }

    protected Set<CurriculumEntry> getCurriculumEntries() {
        if (curriculumEntries == null) {

            if (this.sourceLines != null && !this.sourceLines.isEmpty()) {
                collectByEntries();
            } else {
                collectWithRegistrations();
            }
        }

        return curriculumEntries;
    }

    private void collectByEntries() {
        curriculumEntries = Sets.newTreeSet(CurriculumEntry.NAME_COMPARATOR(locale));
        for (Enrolment curriculumLine : sourceLines) {
            curriculumEntries
                    .addAll(CurriculumEntry.transform(registration, Collections.singleton(curriculumLine), remarksDataProvider));
        }
    }

    private void collectWithRegistrations() {
        curriculumEntries = Sets.newTreeSet(CurriculumEntry.NAME_COMPARATOR(locale));
        final Student student = registration.getStudent();

        for (final Registration registration : student.getActiveRegistrations()) {
            Collection<CurriculumLine> standaloneCurriculumLines = registration.getStandaloneCurriculumLines();
            for (CurriculumLine curriculumLine : standaloneCurriculumLines) {
                Collection<ICurriculumEntry> entries =
                        curriculumLine.getCurriculum(emissionDate.toDateTimeAtStartOfDay()).getCurriculumEntries();
                curriculumEntries.addAll(CurriculumEntry.transform(registration, entries, remarksDataProvider));
            }
        }
    }

}
