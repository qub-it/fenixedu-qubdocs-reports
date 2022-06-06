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
import java.util.Set;
import java.util.function.BiFunction;

import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.qubdocs.util.CurriculumEntryServices;

import com.google.common.collect.Sets;
import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IReportDataProvider;

public class CurriculumEntryRemarksDataProvider implements IReportDataProvider {

    protected static final String KEY = "curriculumEntryRemarks";

    private Set<RemarkEntry> remarkEntries = Sets.newLinkedHashSet();

    protected String key;
    protected Registration registration;

    private ICreditsTransferRemarksCollection creditsTransferRemarks;

    public CurriculumEntryRemarksDataProvider(final Registration registration, final Collection<ICurriculumEntry> entries,
            final CurriculumEntryServices service) {
        this.key = KEY;
        this.registration = registration;
        
        creditsTransferRemarks = service.buildRemarksFor(entries, registration.getLastStudentCurricularPlan());
        creditsTransferRemarks.getRemarkIds().forEach(
                remarkId -> remarkEntries.add(new RemarkEntry(remarkId, creditsTransferRemarks.getRemarkTextForId(remarkId))));
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

    public class RemarkEntry {

        private String remarkNumber;
        private LocalizedString description;

        public RemarkEntry(final String remarkNumber, LocalizedString description) {

            this.remarkNumber = remarkNumber;
            this.description = description;
        }

        public String getRemarkNumber() {
            return remarkNumber;
        }

        public void setRemarkNumber(String remarkNumber) {
            this.remarkNumber = remarkNumber;
        }

        public void setDescription(LocalizedString description) {
            this.description = description;
        }

        public LocalizedString getDescription() {
            return description;
        }

    }

    public String getRemarkIdsFor(CurriculumEntry curriculumEntry) {
        return creditsTransferRemarks.getRemarkIdsFor(curriculumEntry.getICurriculumEntry());
    }

}
