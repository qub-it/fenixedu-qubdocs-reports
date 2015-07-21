package org.fenixedu.qubdocs.task;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentPurposeTypeInstance;
import org.fenixedu.bennu.scheduler.custom.CustomTask;
import org.fenixedu.commons.i18n.LocalizedString;

public class FMVConfigurationScript extends CustomTask {

    final private static Locale pt = new Locale("pt", "PT");
    final private static Locale en = new Locale("en", "GB");

    @Override
    public void runTask() throws Exception {
        Map<String, LocalizedString> documentPurposeTypes = new HashMap<String, LocalizedString>();
        documentPurposeTypes.put("FAMILY_ALLOWANCE", new LocalizedString(pt, "Abono de Família").with(en, "Family Allowance"));
        documentPurposeTypes.put("FAMILY_BENEFITS", new LocalizedString(pt, "Prestações Familiares").with(en, "Family Benefits"));
        documentPurposeTypes.put("IRS", new LocalizedString(pt, "IRS").with(en, "Taxes"));
        documentPurposeTypes.put("ADSE_ADM_SAD_SSMJ", new LocalizedString(pt, "ADSE/ADM/SAD/SSMJ").with(en, "ADSE/ADM/SAD/SSMJ"));
        documentPurposeTypes.put("SOCIAL_SECURITY", new LocalizedString(pt, "Segurança Social").with(en, "Social Security"));
        documentPurposeTypes.put("MILITARY", new LocalizedString(pt, "Fins Militares").with(en, "Military Purposes"));
        documentPurposeTypes.put("SUB23", new LocalizedString(pt, "Sub23").with(en, "Sub23"));
        documentPurposeTypes.put("STUDY_SCHOLARSHIP", new LocalizedString(pt, "Bolsa de Estudo").with(en, "Scholarship"));
        documentPurposeTypes.put("PROFESSIONAL", new LocalizedString(pt, "Fins Profissionais").with(en, "Professional purposes"));
        documentPurposeTypes.put("PPRE", new LocalizedString(pt, "PPRE").with(en, "PPRE"));
        documentPurposeTypes.put("SCHOOL_INSURANCE", new LocalizedString(pt, "Seguro Escolar").with(en, "School Insurance"));
        documentPurposeTypes.put("OTHER", new LocalizedString(pt, "Outra").with(en, "Other"));

        for (Entry<String, LocalizedString> entry : documentPurposeTypes.entrySet()) {
            DocumentPurposeTypeInstance dpti;
            if (DocumentPurposeTypeInstance.findUnique(entry.getKey()) == null) {
                dpti = DocumentPurposeTypeInstance.create(entry.getKey(), entry.getValue());
            } else {
                dpti = DocumentPurposeTypeInstance.findUnique(entry.getKey());
                dpti.setName(entry.getValue());
            }
            dpti.setActive(true);
        }
        DocumentPurposeTypeInstance.findActives().filter(dpti -> !documentPurposeTypes.containsKey(dpti.getCode()))
                .forEach(dpti -> dpti.setActive(false));
    }

}
