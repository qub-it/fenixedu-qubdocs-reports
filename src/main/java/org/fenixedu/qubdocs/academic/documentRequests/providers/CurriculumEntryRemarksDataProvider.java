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
import java.util.Locale;
import java.util.Set;

import org.fenixedu.academic.domain.IEnrolment;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.studentCurriculum.ExternalEnrolment;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.commons.i18n.LocalizedString;

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

    protected RemarkEntry creditsRemarkEntry = new RemarkEntry(String.valueOf(counter++));
    protected Set<RemarkEntry> entries = Sets.newTreeSet(ENTRY_COMPARATOR);

    protected String key;
    protected Registration registration;

    public CurriculumEntryRemarksDataProvider(final Registration registration) {
        entries.add(creditsRemarkEntry);
        this.key = KEY;
        this.registration = registration;
    }

    public CurriculumEntryRemarksDataProvider(final Registration registration, final String key) {
        this(registration);
        this.key = key;
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

        return entries;
    }

    public void addCurriculumEntry(final CurriculumEntry curriculumEntry) {
        boolean addedToInstitution = false;
        for (RemarkEntry remarkEntry : Sets.newHashSet(entries)) {
            addedToInstitution |= remarkEntry.addIf(curriculumEntry);
        }

        if (addedToInstitution) {
            return;
        }

        if (curriculumEntry.isExternal()) {
            addInstitutionEntryFor(curriculumEntry);
        }
    }

    public Set<RemarkEntry> getRemarkEntriesFor(final CurriculumEntry curriculumEntry) {
        return Sets.filter(entries, new Predicate<RemarkEntry>() {
            @Override
            public boolean apply(final RemarkEntry remarkEntry) {
                return remarkEntry.curriculumEntries.contains(curriculumEntry);
            }
        });
    }

    protected void addInstitutionEntryFor(final CurriculumEntry curriculumEntry) {
        entries.add(new RemarkEntry(String.valueOf(counter++), curriculumEntry));
    }

    public enum RemarkEntryType {
        INSTITUTION, CREDITS;

        public String getQualifiedName() {
            return this.getClass().getSimpleName() + "." + name();
        }
    }

    public class RemarkEntry {
        protected String remarkNumber;
        protected Set<CurriculumEntry> curriculumEntries = Sets.newHashSet();
        protected RemarkEntryType type;
        protected Unit institution;

        public RemarkEntry(final String remarkNumber) {
            this.remarkNumber = remarkNumber;
            this.type = RemarkEntryType.CREDITS;
        }

        public RemarkEntry(final String remarkNumber, final CurriculumEntry curriculumEntry) {
            this.remarkNumber = remarkNumber;
            this.institution = ((ExternalEnrolment) curriculumEntry.getICurriculumEntry()).getAcademicUnit();
            this.type = RemarkEntryType.INSTITUTION;
            this.curriculumEntries.add(curriculumEntry);
        }

        public boolean addIf(final CurriculumEntry entry) {
            if (entry.isExternal()) {
                ExternalEnrolment externalEnrolment = (ExternalEnrolment) entry.getICurriculumEntry();
                if (isInstitutionEntry() && externalEnrolment.getAcademicUnit() == institution) {
                    curriculumEntries.add(entry);
                    return true;
                }
            }

            if ((entry.isExternal() || isDismissal(entry)) && isCreditsEntry()) {
                curriculumEntries.add(entry);
                return false;
            }

            return false;
        }

        protected boolean isInstitutionEntry() {
            return RemarkEntryType.INSTITUTION == type;
        }

        protected boolean isCreditsEntry() {
            return RemarkEntryType.CREDITS == type;
        }

        // TODO
        public LocalizedString getDescription() {
            if (curriculumEntries.isEmpty()) {
                LocalizedString mls = new LocalizedString();
                for (Locale locale : CoreConfiguration.supportedLocales()) {
                    mls = mls.with(locale, "");
                }
                return mls;
            }
            LocalizedString mls = new LocalizedString();
            for (Locale locale : CoreConfiguration.supportedLocales()) {
//                String message =
//                        BundleUtil.getStringFromResourceBundle(QubEduAcademicOfficePlugin.BUNDLE, locale,
//                                type.getQualifiedName(), remarkNumber, institution != null ? institution.getNameI18n().getContent() : "");

                mls = mls.with(locale, "");
            }

            return mls;
        }
    }

    // TODO
    public boolean isDismissal(CurriculumEntry entry) {
        if (entry.isDismissal()) {
            return true;
        }

        if (entry.isIEnrolment() && ((IEnrolment) entry.getICurriculumEntry()).isEnrolment()) {
            return false;
            // return ((Enrolment) entry.getICurriculumEntry()).hasAnyDismissal(registration.getLastStudentCurricularPlan());
        }

        return false;
    }

}
