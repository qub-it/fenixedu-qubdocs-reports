package org.fenixedu.qubdocs.academic.documentRequests.providers;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.Locale;
import java.util.Set;

import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;

import com.google.common.collect.Sets;
import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IReportDataProvider;

public class ApprovedCurriculumEntriesDataProvider implements IReportDataProvider {

    protected static final String KEY = "approvedCurriculumEntries";
    protected static final String KEY_FOR_REMARKS = "approvementRemarks";
    protected static final String KEY_FOR_TOTAL_UNITS = "totalApprovements";
    protected static final String KEY_FOR_TOTAL_ECTS = "totalApprovedECTS";

    private Registration registration;
    private CurriculumEntryRemarksDataProvider remarksDataProvider;
    private Locale locale;
    private Collection<ICurriculumEntry> approvements;
    private Set<CurriculumEntry> curriculumEntries;

    public ApprovedCurriculumEntriesDataProvider(final Registration registration,
            final Collection<ICurriculumEntry> approvements, final Locale locale) {
        this.registration = registration;
        this.locale = locale;
        this.remarksDataProvider = new CurriculumEntryRemarksDataProvider(registration);
        this.approvements = approvements;
    }

    @Override
    public void registerFieldsAndImages(IDocumentFieldsData documentFieldsData) {
        documentFieldsData.registerCollectionAsField(KEY);
        documentFieldsData.registerCollectionAsField(KEY_FOR_REMARKS);
    }

    @Override
    public boolean handleKey(final String key) {
        if (approvements == null || approvements.isEmpty()) {
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

    private Set<CurriculumEntry> getCurriculumEntries() {
        if (curriculumEntries == null) {
            final Set<ICurriculumEntry> entries = Sets.newHashSet(approvements);
            curriculumEntries = Sets.newTreeSet(new Comparator<CurriculumEntry>() {

                @Override
                public int compare(final CurriculumEntry left, final CurriculumEntry right) {
                    if (left.getExecutionYear() == right.getExecutionYear()) {
                        return compareByName(left, right);
                    }
                    return left.getExecutionYear().compareTo(right.getExecutionYear());
                }

                public int compareByName(final CurriculumEntry left, final CurriculumEntry right) {
                    String leftContent =
                            left.getName().getContent(locale) != null ? left.getName().getContent(locale) : left.getName()
                                    .getContent();
                    String rightContent =
                            right.getName().getContent(locale) != null ? right.getName().getContent(locale) : right.getName()
                                    .getContent();
                    leftContent = leftContent.toLowerCase();
                    rightContent = rightContent.toLowerCase();

                    return leftContent.compareTo(rightContent);
                }
            });
            curriculumEntries.addAll(CurriculumEntry.transform(registration, entries, remarksDataProvider));
        }
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

}
