package org.fenixedu.qubdocs.domain;

import java.util.Set;

import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixframework.Atomic;

public class DocumentSignature extends DocumentSignature_Base {
    

    protected DocumentSignature() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected DocumentSignature(AdministrativeOffice administrativeOffice, String responsibleName, LocalizedString responsibleFunction, LocalizedString responsibleUnit) {
        this();
        setAdministrativeOffice(administrativeOffice);
        setResponsibleName(responsibleName);
        setResponsibleFunction(responsibleFunction);
        setResponsibleUnit(responsibleUnit);

    }

    private void checkRules() {
    }

    @Atomic
    public void edit(String responsibleName, LocalizedString responsibleFunction, LocalizedString responsibleUnit) {
        setResponsibleName(responsibleName);
        setResponsibleFunction(responsibleFunction);
        setResponsibleUnit(responsibleUnit);
    }

    public boolean isDeletable() {
        return true;
    }

   

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Set<DocumentSignature> readAll() {
        return Bennu.getInstance().getDocumentSignaturesSet();
    }
  

    @Atomic
    public static DocumentSignature create(AdministrativeOffice administrativeOffice, String responsibleName, LocalizedString responsibleFunction, LocalizedString responsibleUnit) {
        return new DocumentSignature(administrativeOffice, responsibleName, responsibleFunction, responsibleUnit);
    }

}


