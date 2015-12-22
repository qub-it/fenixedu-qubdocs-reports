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
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.dto.student.RegistrationConclusionBean;

import com.google.common.collect.Sets;
import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IFieldsExporter;
import com.qubit.terra.docs.util.IReportDataProvider;

public class CurriculumEntriesDataProvider implements IReportDataProvider {

    protected static final String KEY = "curriculumEntries";

    protected Registration registration;
    protected ProgramConclusion programConclusion;
    protected CurriculumEntryRemarksDataProvider remarksDataProvider;
    protected Locale locale;

    protected Set<CurriculumEntry> curriculumEntries;

    public CurriculumEntriesDataProvider(final Registration registration, final ProgramConclusion programConclusion,
            final CurriculumEntryRemarksDataProvider remarksDataProvider, final Locale locale) {
        this.registration = registration;
        this.programConclusion = programConclusion;
        this.remarksDataProvider = remarksDataProvider;
        this.locale = locale;
    }

    @Override
    public void registerFieldsAndImages(IDocumentFieldsData documentFieldsData) {
        documentFieldsData.registerCollectionAsField("curriculumEntries");
    }

    @Override
    public boolean handleKey(final String key) {
        return KEY.equals(key);
    }

    @Override
    public Object valueForKey(final String key) {
        if (handleKey(key)) {
            return getCurriculumEntries();
        }

        return null;
    }

    protected Set<CurriculumEntry> getCurriculumEntries() {
        if (curriculumEntries == null) {
            RegistrationConclusionBean conclusionBean = new RegistrationConclusionBean(this.registration, programConclusion);

            final Set<ICurriculumEntry> curricularYearEntries =
                    Sets.newHashSet(conclusionBean.getCurriculumForConclusion().getCurriculumEntries());

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

            curriculumEntries.addAll(CurriculumEntry.transform(registration, curricularYearEntries, remarksDataProvider));
        }

        return curriculumEntries;
    }

    @Override
    public void registerFieldsMetadata(IFieldsExporter exporter) {
        // TODO Auto-generated method stub

    }
}
