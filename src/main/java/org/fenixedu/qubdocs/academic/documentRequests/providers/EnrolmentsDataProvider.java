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
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.qubdocs.util.CurriculumEntryServices;

import com.google.common.collect.Sets;
import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IReportDataProvider;

public class EnrolmentsDataProvider implements IReportDataProvider {
    protected static final String KEY = "enrolments";

    protected Registration registration;
    protected ExecutionYear executionYear;
    protected Locale locale;
    protected List<Enrolments> data;
    protected CurriculumEntryServices service;

    public EnrolmentsDataProvider(final Registration registration, final Set<ICurriculumEntry> normalEnrolmentsEntries,
            final Set<ICurriculumEntry> standaloneEnrolmentsEntries, final Set<ICurriculumEntry> extracurricularEnrolmentsEntries,
            final Set<ICurriculumEntry> enrolmentsInEnrolState, final ExecutionYear executionYear, final Locale locale,
            final CurriculumEntryServices service) {
        this.registration = registration;
        this.executionYear = executionYear;
        this.locale = locale;
        this.service = service;
        data = new ArrayList<>();
        if (normalEnrolmentsEntries != null && !normalEnrolmentsEntries.isEmpty()) {
            data.add(new Enrolments("normal", locale, registration, normalEnrolmentsEntries, service));
        }
        if (standaloneEnrolmentsEntries != null && !standaloneEnrolmentsEntries.isEmpty()) {
            data.add(new Enrolments("standalone", locale, registration, standaloneEnrolmentsEntries, service));
        }
        if (extracurricularEnrolmentsEntries != null && !extracurricularEnrolmentsEntries.isEmpty()) {
            data.add(new Enrolments("extracurricular", locale, registration, extracurricularEnrolmentsEntries, service));
        }
        if (enrolmentsInEnrolState != null && !enrolmentsInEnrolState.isEmpty()) {
            data.add(new Enrolments("active", locale, registration, enrolmentsInEnrolState, service));
        }
    }

    @Override
    public void registerFieldsAndImages(final IDocumentFieldsData documentFieldsData) {
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

    public static class Enrolments {
        private String hasEnrolmentsKey;
        private String enrolmentsKey;
        private String totalsKey;
        private String ectsKey;
        private String remarksKey;

        private Set<ICurriculumEntry> enrolmentsEntries;
        private TreeSet<CurriculumEntry> curriculumEntries;
        private CurriculumEntryRemarksDataProvider remarksDataProvider;
        private CurriculumEntryServices service;

        public Enrolments(final String type, final Locale locale, final Registration registration,
                final Set<ICurriculumEntry> enrolmentsEntries, final CurriculumEntryServices service) {
            final String casedType = type.substring(0, 1).toUpperCase() + type.substring(1);
            this.hasEnrolmentsKey = "has" + casedType + "Enrolments";
            this.enrolmentsKey = type + "EnrolmentsList";
            this.totalsKey = "total" + casedType + "Enrolments";
            this.ectsKey = "total" + casedType + "ECTS";
            this.remarksKey = type + "EnrolmentsRemarks";

            this.enrolmentsEntries = enrolmentsEntries;
            this.remarksDataProvider = new CurriculumEntryRemarksDataProvider(registration, enrolmentsEntries, service);
            this.curriculumEntries = Sets.newTreeSet(new Comparator<CurriculumEntry>() {

                @Override
                public int compare(final CurriculumEntry left, final CurriculumEntry right) {
                    if (left.getExecutionYear() == right.getExecutionYear()) {
                        return compareBySemester(left, right);
                    }
                    return left.getExecutionYear().compareTo(right.getExecutionYear());
                }

                public int compareBySemester(final CurriculumEntry left, final CurriculumEntry right) {
                    if (left.getExecutionSemester() == right.getExecutionSemester()) {
                        return compareByName(left, right);
                    }
                    return left.getExecutionSemester().compareTo(right.getExecutionSemester());
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
            this.curriculumEntries
                    .addAll(CurriculumEntry.transform(registration, this.enrolmentsEntries, remarksDataProvider, service));
        }

        public Object getValue(final String key) {
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

        public void registerCollections(final IDocumentFieldsData documentFieldsData) {
            documentFieldsData.registerCollectionAsField(enrolmentsKey);
        }

        public boolean hasKey(final String key) {
            return key.equals(hasEnrolmentsKey) || key.equals(enrolmentsKey) || key.equals(totalsKey) || key.equals(ectsKey)
                    || key.equals(remarksKey);
        }
    }

}
