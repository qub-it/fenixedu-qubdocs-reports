/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: diogo.simoes@qub-it.com
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
import java.util.Comparator;
import java.util.Locale;
import java.util.Set;

import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.qubdocs.util.CurriculumEntryServices;

import com.google.common.collect.Sets;
import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IReportDataProvider;

public class ExtraCurriculumEntriesDataProvider implements IReportDataProvider {

    protected static final String KEY = "extraCurriculumEntries";
    protected static final String KEY_FOR_REMARKS = "approvementExtraRemarks";
    protected static final String KEY_FOR_TOTAL_UNITS = "totalExtraApprovements";
    protected static final String KEY_FOR_TOTAL_ECTS = "totalExtraApprovedECTS";

    protected Registration registration;
    protected CurriculumEntryRemarksDataProvider remarksDataProvider;
    protected Locale locale;
    protected Collection<ICurriculumEntry> extracurricularApprovements;
    protected Set<CurriculumEntry> curriculumEntries;
    protected CurriculumEntryServices service;

    public ExtraCurriculumEntriesDataProvider(final Registration registration,
            final Collection<ICurriculumEntry> extracurricularApprovements, final Locale locale,
            final CurriculumEntryServices service) {
        this.registration = registration;
        this.locale = locale;
        this.remarksDataProvider = new CurriculumEntryRemarksDataProvider(registration);
        this.extracurricularApprovements = extracurricularApprovements;
        this.service = service;
        init();
    }

    @Override
    public void registerFieldsAndImages(final IDocumentFieldsData documentFieldsData) {
        documentFieldsData.registerCollectionAsField(KEY);
        documentFieldsData.registerCollectionAsField(KEY_FOR_REMARKS);
    }

    @Override
    public boolean handleKey(final String key) {
        if (extracurricularApprovements == null || extracurricularApprovements.isEmpty()) {
            return false;
        }

        return KEY.equals(key) || KEY_FOR_REMARKS.equals(key) || KEY_FOR_TOTAL_UNITS.equals(key)
                || KEY_FOR_TOTAL_ECTS.equals(key);
    }

    @Override
    public Object valueForKey(final String key) {
        if (key.equals(KEY)) {
            return getCurriculumEntries();
        } else if (key.equals(KEY_FOR_REMARKS)) {
            return getRemarks();
        } else if (key.equals(KEY_FOR_TOTAL_UNITS)) {
            return getTotalApprovements();
        } else if (key.equals(KEY_FOR_TOTAL_ECTS)) {
            return getApprovedEcts();
        } else {
            return null;
        }
    }

    protected Set<CurriculumEntry> getCurriculumEntries() {
        return curriculumEntries;
    }

    private int getTotalApprovements() {
        if (curriculumEntries == null) {
            return 0;
        }
        return curriculumEntries.size();
    }

    private BigDecimal getApprovedEcts() {
        if (curriculumEntries == null) {
            return BigDecimal.ZERO;
        }
        return curriculumEntries.stream().map(CurriculumEntry::getEctsCredits).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Object getRemarks() {
        return remarksDataProvider.valueForKey("curriculumEntryRemarks");
    }

    protected void init() {
        if (extracurricularApprovements != null) {
            final Set<ICurriculumEntry> entries = Sets.newHashSet(extracurricularApprovements);
            curriculumEntries = Sets.newTreeSet(new Comparator<CurriculumEntry>() {

                @Override
                public int compare(final CurriculumEntry left, final CurriculumEntry right) {
                    if (left.getExecutionYear() == right.getExecutionYear()) {
                        return compareByName(left, right);
                    }
                    return left.getExecutionYear().compareTo(right.getExecutionYear());
                }

                public int compareByName(final CurriculumEntry left, final CurriculumEntry right) {
                    String leftContent = left.getName().getContent(locale) != null ? left.getName().getContent(locale) : left
                            .getName().getContent();
                    String rightContent = right.getName().getContent(locale) != null ? right.getName().getContent(locale) : right
                            .getName().getContent();
                    leftContent = leftContent.toLowerCase();
                    rightContent = rightContent.toLowerCase();

                    return leftContent.compareTo(rightContent);
                }
            });
            curriculumEntries.addAll(CurriculumEntry.transform(registration, entries, remarksDataProvider, service));
        }
    }

}
