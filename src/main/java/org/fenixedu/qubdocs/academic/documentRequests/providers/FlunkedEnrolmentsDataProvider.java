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

public class FlunkedEnrolmentsDataProvider implements IReportDataProvider {

    protected static final String KEY = "flunkedCurriculumEntries";
    protected static final String KEY_FOR_REMARKS = "flunkedEnrolmentsRemarks";
    protected static final String KEY_FOR_TOTAL_UNITS = "totalFlunkedEnrolments";
    protected static final String KEY_FOR_TOTAL_ECTS = "totalFlunkedECTS";

    protected Registration registration;
    protected CurriculumEntryRemarksDataProvider remarksDataProvider;
    protected Locale locale;
    protected Collection<ICurriculumEntry> flunkedEnrolments;
    protected Set<CurriculumEntry> curriculumEntries;
    protected CurriculumEntryServices service;

    public FlunkedEnrolmentsDataProvider(final Registration registration, final Collection<ICurriculumEntry> flunkedEnrolments,
            final Locale locale, final CurriculumEntryServices service) {
        this.registration = registration;
        this.locale = locale;
        this.remarksDataProvider = new CurriculumEntryRemarksDataProvider(registration);
        this.flunkedEnrolments = flunkedEnrolments;
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
        if (flunkedEnrolments == null || flunkedEnrolments.isEmpty()) {
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
            return getFlunkedEcts();
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

    private BigDecimal getFlunkedEcts() {
        if (curriculumEntries == null) {
            return BigDecimal.ZERO;
        }
        return curriculumEntries.stream().map(CurriculumEntry::getEctsCredits).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Object getRemarks() {
        return remarksDataProvider.valueForKey("curriculumEntryRemarks");
    }

    protected void init() {
        if (flunkedEnrolments != null) {
            final Set<ICurriculumEntry> entries = Sets.newHashSet(flunkedEnrolments);
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
