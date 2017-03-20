package org.fenixedu.qubdocs.academic.documentRequests.providers;

import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IReportDataProvider;

public class DocumentFooterDataProvider implements IReportDataProvider {

    protected static final String KEY_SHOW_FOOTER = "showFooter";
    protected static final String KEY_SHOW_ISSUED = "showIssued";
    protected static final String KEY_ISSUED_TEXT = "issuedText";
    protected static final String KEY_SHOW_CHECKED_BY = "showCheckedBy";

    protected boolean showFooter;
    protected boolean showIssued;
    protected String issuedText;
    protected boolean showCheckedBy;

    public DocumentFooterDataProvider(final boolean showFooter, final boolean showIssued, final String issuedText,
            final boolean showCheckedBy) {
        this.showFooter = showFooter;
        this.showIssued = showIssued;
        this.issuedText = issuedText;
        this.showCheckedBy = showCheckedBy;
    }

    @Override
    public void registerFieldsAndImages(final IDocumentFieldsData documentFieldsData) {
    }

    @Override
    public boolean handleKey(final String key) {
        return KEY_SHOW_FOOTER.equals(key) || KEY_SHOW_ISSUED.equals(key) || KEY_ISSUED_TEXT.equals(key)
                || KEY_SHOW_CHECKED_BY.equals(key);
    }

    @Override
    public Object valueForKey(final String key) {
        if (KEY_SHOW_FOOTER.equals(key)) {
            return showFooter;
        } else if (KEY_SHOW_ISSUED.equals(key)) {
            return showIssued;
        } else if (KEY_ISSUED_TEXT.equals(key)) {
            return issuedText;
        } else if (KEY_SHOW_CHECKED_BY.equals(key)) {
            return showCheckedBy;
        }
        return null;
    }

}
