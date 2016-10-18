package org.fenixedu.qubdocs.base.providers;

import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IReportDataProvider;

public class InstitutionConfigurationReportDataProvider implements IReportDataProvider {

    protected static final String KEY_LOGO = "institutionLogo";
    protected static final String KEY_NAME = "institutionName";

    private String institutionName;
    private byte[] institutionLogo;

    public InstitutionConfigurationReportDataProvider(final String name, final byte[] image) {
        institutionName = name;
        institutionLogo = image;
    }

    @Override
    public void registerFieldsAndImages(IDocumentFieldsData documentFieldsData) {
        if (institutionLogo.length == 0) {
            return;
        }
        documentFieldsData.registerImage(KEY_LOGO, institutionLogo);
    }

    @Override
    public boolean handleKey(String key) {
        return key.equals(KEY_NAME);
    }

    @Override
    public Object valueForKey(String key) {
        if (key.equals(KEY_NAME)) {
            return institutionName;
        } else {
            return null;
        }
    }

}
