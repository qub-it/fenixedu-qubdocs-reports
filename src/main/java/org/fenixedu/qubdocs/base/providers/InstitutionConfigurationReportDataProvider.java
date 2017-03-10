package org.fenixedu.qubdocs.base.providers;

import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IReportDataProvider;

public class InstitutionConfigurationReportDataProvider implements IReportDataProvider {

    protected static final String KEY_LOGO = "institutionLogo";
    protected static final String KEY_NAME = "institutionName";
    protected static final String KEY_SHORT_NAME = "institutionShortName";
    protected static final String KEY_ADDRESS = "institutionAddress";
    protected static final String KEY_SITE = "institutionSite";

    private final String institutionName;
    private final String institutionShortName;
    private final String institutionAddress;
    private final String institutionSite;
    private final byte[] institutionLogo;

    public InstitutionConfigurationReportDataProvider(final String name, final String shortName, final String address,
            final String site, final byte[] image) {
        institutionName = name;
        institutionShortName = shortName;
        institutionAddress = address;
        institutionSite = site;
        institutionLogo = image;
    }

    @Override
    public void registerFieldsAndImages(final IDocumentFieldsData documentFieldsData) {
        if (institutionLogo.length == 0) {
            return;
        }
        documentFieldsData.registerImage(KEY_LOGO, institutionLogo);
    }

    @Override
    public boolean handleKey(final String key) {
        return key.equals(KEY_NAME) || key.equals(KEY_SHORT_NAME) || key.equals(KEY_ADDRESS) || key.equals(KEY_SITE);
    }

    @Override
    public Object valueForKey(final String key) {
        if (key.equals(KEY_NAME)) {
            return institutionName;
        } else if (key.equals(KEY_SHORT_NAME)) {
            return institutionShortName;
        } else if (key.equals(KEY_SITE)) {
            return institutionSite;
        } else if (key.equals(KEY_ADDRESS)) {
            return institutionAddress;
        } else {
            return null;
        }
    }

}
