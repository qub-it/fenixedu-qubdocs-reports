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

import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequest;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;

import com.google.common.collect.Sets;
import com.qubit.terra.docs.util.IDocumentFieldsData;

public class ApprovementCertificateCurriculumEntries extends CurriculumEntriesDataProvider {

    protected static final String KEY = "approvementCertificateCurriculumEntries";

    private DocumentRequest certificateRequest;

    public ApprovementCertificateCurriculumEntries(final DocumentRequest certificateRequest, final Registration registration,
            final CycleType cycleType, final CurriculumEntryRemarksDataProvider remarksDataProvider, final Locale locale) {
        super(registration, cycleType, remarksDataProvider, locale);

        this.certificateRequest = certificateRequest;
    }

    @Override
    public void registerFieldsAndImages(IDocumentFieldsData documentFieldsData) {
        documentFieldsData.registerCollectionAsField(KEY);
    }

    // TODO
    @Override
    protected Set<CurriculumEntry> getCurriculumEntries() {

        if (curriculumEntries == null) {

            final Set<ICurriculumEntry> entries = Sets.newHashSet(certificateRequest.getApprovedCurriculumEntries());

            curriculumEntries = Sets.newTreeSet(new Comparator<CurriculumEntry>() {

                @Override
                public int compare(final CurriculumEntry left, final CurriculumEntry right) {
                    final String leftContent =
                            left.getName().getContent(locale) != null ? left.getName().getContent(locale) : left.getName()
                                    .getContent();
                    final String rightContent =
                            right.getName().getContent(locale) != null ? right.getName().getContent(locale) : right.getName()
                                    .getContent();

                    return leftContent.compareTo(rightContent);
                }
            });

            curriculumEntries.addAll(CurriculumEntry.transform(registration, entries, remarksDataProvider));
        }

        return curriculumEntries;
    }

    @Override
    public boolean handleKey(final String key) {
        return KEY.equals(key);
    }

}
