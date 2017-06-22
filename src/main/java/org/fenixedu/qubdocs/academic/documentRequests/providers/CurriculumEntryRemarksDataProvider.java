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

import java.util.Comparator;
import java.util.Set;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.IEnrolment;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.studentCurriculum.ExternalEnrolment;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.commons.i18n.LocalizedString.Builder;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IReportDataProvider;

public class CurriculumEntryRemarksDataProvider implements IReportDataProvider {

    public static final Comparator<RemarkEntry> ENTRY_COMPARATOR = new Comparator<RemarkEntry>() {

        @Override
        public int compare(RemarkEntry o1, RemarkEntry o2) {
            return o1.remarkNumber.compareTo(o2.remarkNumber);
        }

    };

    protected static final String KEY = "curriculumEntryRemarks";
    protected char counter = 'a';

    private Set<RemarkEntry> remarkEntries = Sets.newTreeSet(ENTRY_COMPARATOR);

    protected String key;
    protected Registration registration;

    public CurriculumEntryRemarksDataProvider(final Registration registration) {
        this.key = KEY;
        this.registration = registration;
    }

    @Override
    public void registerFieldsAndImages(IDocumentFieldsData documentFieldsData) {

    }

    @Override
    public boolean handleKey(final String key) {
        return this.key.equals(key);
    }

    @Override
    public Object valueForKey(final String key) {
        if (!handleKey(key)) {
            return null;
        }

        return remarkEntries;
    }

    protected RemarkEntry addCurriculumEntryToRemarkEntry(final CurriculumEntry input) {
        RemarkEntry result = null;

        final LocalizedString description = input.getCurriculumEntryDescription();
        if (description != null) {

            for (final RemarkEntry iter : Sets.newHashSet(remarkEntries)) {
                if (iter.addIf(input)) {
                    result = iter;
                    break;
                }
            }

            if (result == null) {

                final RemarkEntryType remarkEntryType = getRemarkEntryType(input);
                if (remarkEntryType != null) {
                    result = new RemarkEntry(String.valueOf(counter++), remarkEntryType, input);

                    remarkEntries.add(result);
                }
            }
        }

        return result;
    }

    public Set<RemarkEntry> getRemarkEntriesFor(final CurriculumEntry curriculumEntry) {
        return Sets.filter(remarkEntries, new Predicate<RemarkEntry>() {
            @Override
            public boolean apply(final RemarkEntry remarkEntry) {
                return remarkEntry.curriculumEntries.contains(curriculumEntry);
            }
        });
    }

    public enum RemarkEntryType {
        INSTITUTION, CREDITS;

        public String getQualifiedName() {
            return this.getClass().getSimpleName() + "." + name();
        }
    }

    public class RemarkEntry {
        protected String remarkNumber;
        private Set<CurriculumEntry> curriculumEntries = Sets.newHashSet();
        private RemarkEntryType type;
        private LocalizedString curriculumEntryDescription;

        public RemarkEntry(final String remarkNumber, final RemarkEntryType remarkEntryType,
                final CurriculumEntry curriculumEntry) {

            this.remarkNumber = remarkNumber;
            this.type = remarkEntryType;
            this.curriculumEntries.add(curriculumEntry);
        }

        public boolean addIf(final CurriculumEntry entry) {
            return entry.getCurriculumEntryDescription().equals(getCurriculumEntryDescription()) && curriculumEntries.add(entry);
        }

        protected boolean isInstitutionEntry() {
            return RemarkEntryType.INSTITUTION == type;
        }

        protected boolean isCreditsEntry() {
            return RemarkEntryType.CREDITS == type;
        }

        public LocalizedString getDescription() {
            LocalizedString result = getCurriculumEntryDescription();

            final Builder builder = new LocalizedString.Builder();
            result.forEach((locale, value) -> builder.with(locale, remarkNumber + ") " + value));
            result = builder.build();

            return result;
        }

        private LocalizedString getCurriculumEntryDescription() {
            if (curriculumEntryDescription == null) {
                curriculumEntryDescription = new LocalizedString();

                if (!curriculumEntries.isEmpty()) {
                    // all entries should share the same description
                    curriculumEntryDescription = curriculumEntries.iterator().next().getCurriculumEntryDescription();
                }
            }

            return curriculumEntryDescription;
        }

    }

    public RemarkEntryType getRemarkEntryType(final CurriculumEntry entry) {

        if (entry.isExternal()) {
            final ExternalEnrolment externalEnrolment = (ExternalEnrolment) entry.getICurriculumEntry();
            if (externalEnrolment.getAcademicUnit() != null) {
                return RemarkEntryType.INSTITUTION;
            }
        }

        if (entry.isExternal() || isDismissal(entry)) {
            return RemarkEntryType.CREDITS;
        }

        return null;
    }

    public boolean isDismissal(CurriculumEntry entry) {
        if (entry.isDismissal()) {
            return true;
        }

        if (entry.isIEnrolment() && ((IEnrolment) entry.getICurriculumEntry()).isEnrolment()) {
            return hasAnyDismissal(registration.getLastStudentCurricularPlan(), (Enrolment) entry.getICurriculumEntry());
        }

        return false;
    }

    private boolean hasAnyDismissal(StudentCurricularPlan studentCurricularPlan, Enrolment enrolment) {
        return studentCurricularPlan.getCreditsSet().stream()
                .anyMatch(c -> c.getEnrolmentsSet().stream().anyMatch(ew -> ew.getIEnrolment() == enrolment));
    }

}
