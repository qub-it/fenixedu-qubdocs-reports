package org.fenixedu.qubdocs.domain;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class InstitutionReportConfiguration extends InstitutionReportConfiguration_Base {

    public InstitutionReportConfiguration() {
        super();
        setBennu(Bennu.getInstance());
    }

    public void delete() {
        if (getInstitutionLogo() != null) {
            getInstitutionLogo().delete();
        }
        if (getLetterheadInstitutionLogo() != null) {
            getLetterheadInstitutionLogo().delete();
        }

        setBennu(null);

        deleteDomainObject();
    }

    @Atomic(mode = TxMode.SPECULATIVE_READ)
    public static InstitutionReportConfiguration getInstance() {
        if (Bennu.getInstance().getInstitutionReportConfiguration() == null) {
            return initialize();
        }
        return Bennu.getInstance().getInstitutionReportConfiguration();
    }

    private static InstitutionReportConfiguration initialize() {
        if (Bennu.getInstance().getInstitutionReportConfiguration() == null) {
            return new InstitutionReportConfiguration();
        }
        return Bennu.getInstance().getInstitutionReportConfiguration();
    }

}
