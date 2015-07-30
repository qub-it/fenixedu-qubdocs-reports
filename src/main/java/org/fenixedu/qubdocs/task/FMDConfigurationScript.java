package org.fenixedu.qubdocs.task;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.person.Gender;
import org.fenixedu.academic.domain.serviceRequests.ServiceRequestType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentPurposeType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentPurposeTypeInstance;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentSigner;
import org.fenixedu.bennu.scheduler.custom.CustomTask;
import org.fenixedu.commons.i18n.LocalizedString;

public class FMDConfigurationScript extends CustomTask {

    final private static Locale pt = new Locale("pt", "PT");
    final private static Locale en = new Locale("en", "GB");

    @Override
    public void runTask() throws Exception {
        Map<String, LocalizedString> documentPurposeTypes = new HashMap<String, LocalizedString>();
        /*
        documentPurposeTypes.put("FAMILY_BENEFITS", new LocalizedString(pt, "Prestações Familiares").with(en, "Family Benefits"));
        documentPurposeTypes.put("IRS", new LocalizedString(pt, "IRS").with(en, "Taxes"));
        documentPurposeTypes.put("ADSE", new LocalizedString(pt, "ADSE").with(en, "ADSE"));
        documentPurposeTypes.put("SOCIAL_SECURITY", new LocalizedString(pt, "Segurança Social").with(en, "Social Security"));
        documentPurposeTypes.put("MILITARY", new LocalizedString(pt, "Fins Militares").with(en, "Military Purposes"));
        documentPurposeTypes.put("PUBLIC_TRANSPORTS",
                new LocalizedString(pt, "Transportes Públicos").with(en, "Public Transports"));
        documentPurposeTypes.put("STUDY_SCHOLARSHIP", new LocalizedString(pt, "Bolsa de Estudo").with(en, "Scholarship"));
        documentPurposeTypes.put("PROFESSIONAL", new LocalizedString(pt, "Fins Profissionais").with(en, "Professional purposes"));
        documentPurposeTypes.put("PPRE", new LocalizedString(pt, "PPRE").with(en, "PPRE"));
        documentPurposeTypes.put("OTHER", new LocalizedString(pt, "Outra").with(en, "Other"));
        */

        for (Entry<String, LocalizedString> entry : documentPurposeTypes.entrySet()) {
            DocumentPurposeTypeInstance dpti;
            if (DocumentPurposeTypeInstance.findUnique(entry.getKey()) == null) {
                // Enum availability must be tested through Exception handling unfortunetely
                try {
                    DocumentPurposeType documentPurposeType = DocumentPurposeType.valueOf(entry.getKey());
                    dpti = DocumentPurposeTypeInstance.create(entry.getKey(), entry.getValue(), documentPurposeType);
                } catch (IllegalArgumentException iae) {
                    dpti = DocumentPurposeTypeInstance.create(entry.getKey(), entry.getValue());
                }
            } else {
                dpti = DocumentPurposeTypeInstance.findUnique(entry.getKey());
                dpti.setName(entry.getValue());
            }
            dpti.setActive(true);
            for (ServiceRequestType srt : dpti.getServiceRequestTypesSet()) {
                dpti.removeServiceRequestTypes(srt);
            }
            for (ServiceRequestType srt : Stream.concat(ServiceRequestType.findDeclarations(),
                    ServiceRequestType.findCertificates()).collect(Collectors.toList())) {
                dpti.addServiceRequestTypes(srt);
            }
        }
        DocumentPurposeTypeInstance.findActives().filter(dpti -> !documentPurposeTypes.containsKey(dpti.getCode()))
                .forEach(dpti -> dpti.setActive(false));

        /* DocumentSigners */
        DocumentSigner.create(AdministrativeOffice.readDegreeAdministrativeOffice(), "Conceição Manso", new LocalizedString(pt,
                "Responsável dos Serviços Académicos"), new LocalizedString(pt, "Faculdade de Medicina Dentária"), Gender.FEMALE);
        DocumentSigner.create(AdministrativeOffice.readDegreeAdministrativeOffice(), "Eduardo Brunheta", new LocalizedString(pt,
                "Assistente Técnico"), new LocalizedString(pt, "Faculdade de Medicina Dentária"), Gender.MALE);
        DocumentSigner.create(AdministrativeOffice.readDegreeAdministrativeOffice(), "Cristina Fernandes", new LocalizedString(
                pt, "Diretora Executiva"), new LocalizedString(pt, "Faculdade de Medicina Dentária"), Gender.FEMALE);
    }

}