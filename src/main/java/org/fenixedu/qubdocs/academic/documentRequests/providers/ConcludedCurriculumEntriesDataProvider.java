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

public class ConcludedCurriculumEntriesDataProvider implements IReportDataProvider {

    protected static final String KEY = "concludedCurriculumEntries";
    protected static final String KEY_FOR_REMARKS = "conclusionRemarks";
    protected static final String KEY_FOR_TOTAL_UNITS = "totalConclusions";
    protected static final String KEY_FOR_TOTAL_ECTS = "totalConcludedECTS";

    private final Registration registration;
    private final CurriculumEntryRemarksDataProvider remarksDataProvider;
    private final Locale locale;
    private final Collection<ICurriculumEntry> conclusions;
    private Set<CurriculumEntry> curriculumEntries;
    private final CurriculumEntryServices service;

    public ConcludedCurriculumEntriesDataProvider(final Registration registration, final Collection<ICurriculumEntry> conclusions,
            final Locale locale, final CurriculumEntryServices service) {
        this.registration = registration;
        this.locale = locale;
        this.remarksDataProvider = new CurriculumEntryRemarksDataProvider(registration);
        this.conclusions = conclusions;
        this.service = service;
    }

    @Override
    public void registerFieldsAndImages(final IDocumentFieldsData documentFieldsData) {
        documentFieldsData.registerCollectionAsField(KEY);
        documentFieldsData.registerCollectionAsField(KEY_FOR_REMARKS);
    }

    @Override
    public boolean handleKey(final String key) {
        if (conclusions == null || conclusions.isEmpty()) {
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
            return getTotalConclusions();
        } else if (key.equals(KEY_FOR_TOTAL_ECTS)) {
            return getConcludedEcts();
        } else {
            return null;
        }
    }

    private Set<CurriculumEntry> getCurriculumEntries() {
        if (curriculumEntries == null) {
            final Set<ICurriculumEntry> entries = Sets.newHashSet(conclusions);
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

                    int result = leftContent.compareTo(rightContent);
                    if (result == 0) {
                        return compareByCode(left, right);
                    }
                    return result;
                }

                public int compareByCode(final CurriculumEntry left, final CurriculumEntry right) {
                    String leftCode = left.getICurriculumEntry().getCode();
                    String rightCode = right.getICurriculumEntry().getCode();

                    int result = leftCode.compareTo(rightCode);
                    if (result == 0) {
                        return compareByExternalId(left, right);
                    }
                    return result;
                }

                public int compareByExternalId(final CurriculumEntry left, final CurriculumEntry right) {
                    String leftExternalId = left.getICurriculumEntry().getExternalId();
                    String rightExternalId = right.getICurriculumEntry().getExternalId();

                    return leftExternalId.compareTo(rightExternalId);
                }
            });
            curriculumEntries.addAll(CurriculumEntry.transform(registration, entries, remarksDataProvider, service));
        }
        return curriculumEntries;
    }

    private int getTotalConclusions() {
        if (curriculumEntries == null) {
            return 0;
        }
        return curriculumEntries.size();
    }

    private BigDecimal getConcludedEcts() {
        if (curriculumEntries == null) {
            return BigDecimal.ZERO;
        }
        return curriculumEntries.stream().map(CurriculumEntry::getEctsCredits).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Object getRemarks() {
        return remarksDataProvider.valueForKey("curriculumEntryRemarks");
    }

}
