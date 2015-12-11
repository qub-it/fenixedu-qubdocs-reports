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
import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;

import com.google.common.collect.Sets;
import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IFieldsExporter;
import com.qubit.terra.docs.util.IReportDataProvider;

public class EnrolmentsDataProvider implements IReportDataProvider {
    protected static final String KEY = "enrolments";
    protected static final String KEY_FOR_LIST = "enrolmentsList";
    protected static final String KEY_FOR_REMARKS = "remarksList";
    protected static final String KEY_FOR_NORMAL_ENROLMENTS = "normalEnrolmentsList";
    protected static final String KEY_FOR_NORMAL_REMARKS = "normalEnrolmentsRemarks";
    protected static final String KEY_FOR_TOTAL_NORMAL_ENROLMENTS = "totalNormalEnrolments";
    protected static final String KEY_FOR_TOTAL_NORMAL_ECTS = "totalNormalECTS";
    protected static final String KEY_FOR_EXTRA_ENROLMENTS = "extraEnrolmentsList";
    protected static final String KEY_FOR_EXTRA_REMARKS = "extraEnrolmentsRemarks";
    protected static final String KEY_FOR_STANDALONE_ENROLMENTS = "standaloneEnrolmentsList";
    protected static final String KEY_FOR_STANDALONE_REMARKS = "standaloneEnrolmentsRemarks";
    protected static final String KEY_FOR_TOTAL_STANDALONE_ENROLMENTS = "totalStandaloneEnrolments";
    protected static final String KEY_FOR_TOTAL_STANDALONE_ECTS = "totalStandaloneECTS";
    protected static final String KEY_HAS_EXTRA_ENROLMENTS = "hasExtraEnrolments";
    protected static final String KEY_HAS_STANDALONE_ENROLMENTS = "hasStandaloneEnrolments";

    protected Registration registration;
    protected Set<ICurriculumEntry> enrolmentsEntries;
    protected ExecutionYear executionYear;
    protected Locale locale;
    protected TreeSet<CurriculumEntry> curriculumEntries;
    protected TreeSet<CurriculumEntry> normalCurriculumEntries;
    protected TreeSet<CurriculumEntry> extraCurriculumEntries;
    protected TreeSet<CurriculumEntry> standaloneCurriculumEntries;

    protected CurriculumEntryRemarksDataProvider remarksDataProvider = null;
    protected CurriculumEntryRemarksDataProvider normalRemarksDataProvider = null;
    protected CurriculumEntryRemarksDataProvider extraRemarksDataProvider = null;
    protected CurriculumEntryRemarksDataProvider standaloneRemarksDataProvider = null;

    public EnrolmentsDataProvider(final Registration registration, final Set<ICurriculumEntry> enrolmentsEntries,
            final ExecutionYear executionYear, final Locale locale) {
        this.registration = registration;
        this.enrolmentsEntries = enrolmentsEntries;
        this.executionYear = executionYear;
        this.locale = locale;
        this.remarksDataProvider = new CurriculumEntryRemarksDataProvider(registration);
        this.normalRemarksDataProvider = new CurriculumEntryRemarksDataProvider(registration);
        this.extraRemarksDataProvider = new CurriculumEntryRemarksDataProvider(registration);
        this.standaloneRemarksDataProvider = new CurriculumEntryRemarksDataProvider(registration);
    }

    @Override
    public void registerFieldsAndImages(IDocumentFieldsData documentFieldsData) {
        documentFieldsData.registerCollectionAsField(KEY_FOR_LIST);
        documentFieldsData.registerCollectionAsField(KEY_FOR_NORMAL_ENROLMENTS);
        documentFieldsData.registerCollectionAsField(KEY_FOR_EXTRA_ENROLMENTS);
        documentFieldsData.registerCollectionAsField(KEY_FOR_STANDALONE_ENROLMENTS);
    }

    @Override
    public boolean handleKey(final String key) {
        return KEY.equals(key) || KEY_FOR_LIST.equals(key) || KEY_FOR_REMARKS.equals(key)
                || KEY_FOR_NORMAL_ENROLMENTS.equals(key) || KEY_FOR_TOTAL_NORMAL_ENROLMENTS.equals(key)
                || KEY_FOR_NORMAL_REMARKS.equals(key) || KEY_FOR_TOTAL_NORMAL_ECTS.equals(key)
                || KEY_FOR_EXTRA_ENROLMENTS.equals(key) || KEY_FOR_EXTRA_REMARKS.equals(key)
                || KEY_FOR_STANDALONE_ENROLMENTS.equals(key) || KEY_FOR_STANDALONE_REMARKS.equals(key)
                || KEY_FOR_TOTAL_STANDALONE_ENROLMENTS.equals(key) || KEY_FOR_TOTAL_STANDALONE_ECTS.equals(key)
                || KEY_HAS_EXTRA_ENROLMENTS.equals(key) || KEY_HAS_STANDALONE_ENROLMENTS.equals(key);
    }

    @Override
    public Object valueForKey(final String key) {
        if (KEY.equals(key)) {
            return this;
        } else if (KEY_FOR_LIST.equals(key)) {
            return this.getCurriculumEntries();
        } else if (KEY_FOR_REMARKS.equals(key)) {
            return this.remarksDataProvider.valueForKey("curriculumEntryRemarks");
        } else if (KEY_FOR_NORMAL_ENROLMENTS.equals(key)) {
            return this.getNormalCurriculumEntries();
        } else if (KEY_FOR_NORMAL_REMARKS.equals(key)) {
            return this.normalRemarksDataProvider.valueForKey("curriculumEntryRemarks");
        } else if (KEY_FOR_TOTAL_NORMAL_ENROLMENTS.equals(key)) {
            return getTotalNormalCurriculumEntries();
        } else if (KEY_FOR_TOTAL_NORMAL_ECTS.equals(key)) {
            return getTotalNormalECTS();
        } else if (KEY_FOR_EXTRA_ENROLMENTS.equals(key)) {
            return this.getExtraCurriculumEntries();
        } else if (KEY_FOR_EXTRA_REMARKS.equals(key)) {
            return this.extraRemarksDataProvider.valueForKey("curriculumEntryRemarks");
        } else if (KEY_FOR_STANDALONE_ENROLMENTS.equals(key)) {
            return this.getStandaloneCurriculumEntries();
        } else if (KEY_FOR_STANDALONE_REMARKS.equals(key)) {
            return this.standaloneRemarksDataProvider.valueForKey("curriculumEntryRemarks");
        } else if (KEY_FOR_TOTAL_STANDALONE_ENROLMENTS.equals(key)) {
            return getTotalStandaloneCurriculumEntries();
        } else if (KEY_FOR_TOTAL_STANDALONE_ECTS.equals(key)) {
            return getTotalStandaloneECTS();
        } else if (KEY_HAS_EXTRA_ENROLMENTS.equals(key)) {
            return !this.getExtraCurriculumEntries().isEmpty();
        } else if (KEY_HAS_STANDALONE_ENROLMENTS.equals(key)) {
            return !this.getStandaloneCurriculumEntries().isEmpty();
        }

        return null;
    }

    public Set<CurriculumEntry> getCurriculumEntries() {
        if (curriculumEntries == null) {
            curriculumEntries = Sets.newTreeSet(CurriculumEntry.NAME_COMPARATOR(locale));

            Collection<? extends ICurriculumEntry> enrolments = enrolmentsEntries;
            curriculumEntries.addAll(CurriculumEntry.transform(registration, enrolments, remarksDataProvider));
        }

        return curriculumEntries;
    }

    public Set<CurriculumEntry> getStandaloneCurriculumEntries() {
        if (standaloneCurriculumEntries == null) {
            standaloneCurriculumEntries = Sets.newTreeSet(CurriculumEntry.NAME_COMPARATOR(locale));

            Collection<? extends ICurriculumEntry> enrolments =
                    enrolmentsEntries.stream().map(Enrolment.class::cast).filter(e -> e.isStandalone())
                            .collect(Collectors.toSet());
            standaloneCurriculumEntries
                    .addAll(CurriculumEntry.transform(registration, enrolments, standaloneRemarksDataProvider));
        }

        return standaloneCurriculumEntries;
    }

    public int getTotalStandaloneCurriculumEntries() {
        return getStandaloneCurriculumEntries().size();
    }

    public BigDecimal getTotalStandaloneECTS() {
        return getStandaloneCurriculumEntries().stream().map(CurriculumEntry::getEctsCredits)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Set<CurriculumEntry> getExtraCurriculumEntries() {
        if (extraCurriculumEntries == null) {
            extraCurriculumEntries = Sets.newTreeSet(CurriculumEntry.NAME_COMPARATOR(locale));

            Collection<? extends ICurriculumEntry> enrolments =
                    enrolmentsEntries.stream().map(Enrolment.class::cast).filter(e -> e.isExtraCurricular())
                            .collect(Collectors.toSet());
            extraCurriculumEntries.addAll(CurriculumEntry.transform(registration, enrolments, extraRemarksDataProvider));
        }

        return extraCurriculumEntries;
    }

    public Set<CurriculumEntry> getNormalCurriculumEntries() {
        if (normalCurriculumEntries == null) {
            normalCurriculumEntries = Sets.newTreeSet(CurriculumEntry.NAME_COMPARATOR(locale));

            Collection<? extends ICurriculumEntry> enrolments =
                    enrolmentsEntries.stream().map(Enrolment.class::cast)
                            .filter(e -> !e.isExtraCurricular() && !e.isPropaedeutic() && !e.isStandalone())
                            .collect(Collectors.toSet());
            normalCurriculumEntries.addAll(CurriculumEntry.transform(registration, enrolments, remarksDataProvider));
        }

        return normalCurriculumEntries;
    }

    public int getTotalNormalCurriculumEntries() {
        return getNormalCurriculumEntries().size();
    }

    public BigDecimal getTotalNormalECTS() {
        return getNormalCurriculumEntries().stream().map(CurriculumEntry::getEctsCredits)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    protected boolean standaloneRegistration() {
        return registration.getDegree().isEmpty();
    }

    @Override
    public void registerFieldsMetadata(IFieldsExporter exporter) {
        // TODO Auto-generated method stub

    }

}
