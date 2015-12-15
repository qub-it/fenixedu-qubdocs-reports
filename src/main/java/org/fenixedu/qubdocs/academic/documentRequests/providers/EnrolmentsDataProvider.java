/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: anil.mamede@qub-it.com
 *               joao.amaral@qub-it.com
 *               diogo.simoes@qub-it.com
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

    protected Registration registration;
    protected ExecutionYear executionYear;
    protected Locale locale;
    protected List<Enrolments> data;

    public EnrolmentsDataProvider(final Registration registration, final Set<ICurriculumEntry> normalEnrolmentsEntries,
            final Set<ICurriculumEntry> standaloneEnrolmentsEntries,
            final Set<ICurriculumEntry> extracurricularEnrolmentsEntries, final ExecutionYear executionYear, final Locale locale) {
        this.registration = registration;
        this.executionYear = executionYear;
        this.locale = locale;
        data = new ArrayList<Enrolments>();
        if (normalEnrolmentsEntries != null && !normalEnrolmentsEntries.isEmpty()) {
            data.add(new Enrolments("normal", locale, registration, normalEnrolmentsEntries));
        }
        if (standaloneEnrolmentsEntries != null && !standaloneEnrolmentsEntries.isEmpty()) {
            data.add(new Enrolments("standalone", locale, registration, standaloneEnrolmentsEntries));
        }
        if (extracurricularEnrolmentsEntries != null && !extracurricularEnrolmentsEntries.isEmpty()) {
            data.add(new Enrolments("extracurricular", locale, registration, extracurricularEnrolmentsEntries));
        }
    }

    @Override
    public void registerFieldsAndImages(IDocumentFieldsData documentFieldsData) {
        for (Enrolments enrolments : data) {
            enrolments.registerCollections(documentFieldsData);
        }
    }

    @Override
    public boolean handleKey(final String key) {
        for (Enrolments enrolments : data) {
            if (enrolments.hasKey(key)) {
                return true;
            }
        }
        return KEY.equals(key);
    }

    @Override
    public Object valueForKey(final String key) {
        if (KEY.equals(key)) {
            return this;
        }
        for (Enrolments enrolments : data) {
            if (enrolments.hasKey(key)) {
                return enrolments.getValue(key);
            }
        }
        return null;
    }

    @Override
    public void registerFieldsMetadata(IFieldsExporter exporter) {
        // TODO Auto-generated method stub

    }

    public static class Enrolments {
        private String hasEnrolmentsKey;
        private String enrolmentsKey;
        private String totalsKey;
        private String ectsKey;
        private String remarksKey;

        private Set<ICurriculumEntry> enrolmentsEntries;
        private TreeSet<CurriculumEntry> curriculumEntries;
        private CurriculumEntryRemarksDataProvider remarksDataProvider;

        public Enrolments(final String type, final Locale locale, final Registration registration,
                final Set<ICurriculumEntry> enrolmentsEntries) {
            final String casedType = type.substring(0, 1).toUpperCase() + type.substring(1);
            this.hasEnrolmentsKey = "has" + casedType + "Enrolments";
            this.enrolmentsKey = type + "EnrolmentsList";
            this.totalsKey = "total" + casedType + "Enrolments";
            this.ectsKey = "total" + casedType + "ECTS";
            this.remarksKey = type + "EnrolmentsRemarks";

            this.enrolmentsEntries = enrolmentsEntries;
            this.remarksDataProvider = new CurriculumEntryRemarksDataProvider(registration);
            this.curriculumEntries = Sets.newTreeSet(CurriculumEntry.NAME_COMPARATOR(locale));
            this.curriculumEntries.addAll(CurriculumEntry.transform(registration, this.enrolmentsEntries, remarksDataProvider));
        }

        public Object getValue(String key) {
            if (key.equals(hasEnrolmentsKey)) {
                return hasEnrolements();
            } else if (key.equals(enrolmentsKey)) {
                return getCurriculumEntries();
            } else if (key.equals(totalsKey)) {
                return getTotalEntries();
            } else if (key.equals(ectsKey)) {
                return getTotalEcts();
            } else if (key.equals(remarksKey)) {
                return getRemarks();
            }
            return null;
        }

        private boolean hasEnrolements() {
            return curriculumEntries.size() > 0;
        }

        private TreeSet<CurriculumEntry> getCurriculumEntries() {
            return curriculumEntries;
        }

        private int getTotalEntries() {
            return curriculumEntries.size();
        }

        private BigDecimal getTotalEcts() {
            return curriculumEntries.stream().map(CurriculumEntry::getEctsCredits).reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        private Object getRemarks() {
            return remarksDataProvider.valueForKey("curriculumEntryRemarks");
        }

        public void registerCollections(IDocumentFieldsData documentFieldsData) {
            documentFieldsData.registerCollectionAsField(enrolmentsKey);
        }

        public boolean hasKey(String key) {
            return key.equals(hasEnrolmentsKey) || key.equals(enrolmentsKey) || key.equals(totalsKey) || key.equals(ectsKey)
                    || key.equals(remarksKey);
        }
    }

}
