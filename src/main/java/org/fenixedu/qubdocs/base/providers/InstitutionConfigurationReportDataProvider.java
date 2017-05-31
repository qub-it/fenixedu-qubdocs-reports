package org.fenixedu.qubdocs.base.providers;

import java.util.Locale;

import org.fenixedu.academic.domain.person.Gender;
import org.fenixedu.commons.i18n.LocalizedString;

import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IReportDataProvider;

import fr.opensagres.xdocreport.template.formatter.NullImageBehaviour;

public class InstitutionConfigurationReportDataProvider implements IReportDataProvider {

    protected static final String KEY_LOGO = "institutionLogo";
    protected static final String KEY_NAME = "institutionName";
    protected static final String KEY_SHORT_NAME = "institutionShortName";
    protected static final String KEY_ADDRESS = "institutionAddress";
    protected static final String KEY_SITE = "institutionSite";
    protected static final String KEY_PREPOSITION = "institutionGender";

    private final LocalizedString institutionName;
    private final LocalizedString institutionShortName;
    private final String institutionAddress;
    private final String institutionSite;
    private final byte[] institutionLogo;
    private final boolean showLogo;
    private final Locale language;

    public InstitutionConfigurationReportDataProvider(final LocalizedString name, final LocalizedString shortName,
            final String address, final String site, final byte[] image, final boolean showLogo, final Locale language) {
        institutionName = name;
        institutionShortName = shortName;
        institutionAddress = address;
        institutionSite = site;
        institutionLogo = image;
        this.showLogo = showLogo;
        this.language = language;
    }

    @Override
    public void registerFieldsAndImages(final IDocumentFieldsData documentFieldsData) {
        if (institutionLogo.length == 0 || !showLogo) {
            documentFieldsData.registerImageNullBehaviour(KEY_LOGO, NullImageBehaviour.RemoveImageTemplate);
            return;
        }
        documentFieldsData.registerImage(KEY_LOGO, institutionLogo);
    }

    @Override
    public boolean handleKey(final String key) {
        return key.equals(KEY_NAME) || key.equals(KEY_SHORT_NAME) || key.equals(KEY_ADDRESS) || key.equals(KEY_SITE)
                || key.equals(KEY_PREPOSITION);
    }

    @Override
    public Object valueForKey(final String key) {
        if (key.equals(KEY_NAME)) {
            return institutionName.getContent(language);
        } else if (key.equals(KEY_SHORT_NAME)) {
            return institutionShortName.getContent(language);
        } else if (key.equals(KEY_SITE)) {
            return institutionSite;
        } else if (key.equals(KEY_ADDRESS)) {
            return institutionAddress;
        } else if (key.equals(KEY_PREPOSITION)) {
            return getPreposition();
        } else {
            return null;
        }
    }

    public Gender getPreposition() {
        if (institutionName.getContent(language).startsWith("Inst")) {
            return Gender.MALE;
        } else {
            return Gender.FEMALE;
        }
    }

}
